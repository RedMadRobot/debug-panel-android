package com.redmadrobot.flipper_plugin.data

import android.content.Context
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper.config.FlipperValue.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

internal class FeatureTogglesRepository(
    private val context: Context,
    private val defaultFeatureToggles: Map<Feature, FlipperValue>,
) {
    companion object {
        private const val TOGGLES_FILE_NAME = "com.redmadrobot.flipper_plugin.toggles"
    }

    private val changedTogglesState = MutableStateFlow(emptyMap<Feature, FlipperValue>())

    init {
        CoroutineScope(Dispatchers.IO).launch { restoreTogglesState() }
    }

    fun observeChangedToggles(): Flow<Map<Feature, FlipperValue>> = changedTogglesState

    fun getFeatureToggles(): Map<Feature, FlipperValue> {
        return defaultFeatureToggles + changedTogglesState.value
    }

    suspend fun saveFeatureState(feature: Feature, value: FlipperValue) {
        changedTogglesState.value = if (defaultFeatureToggles[feature] != value) {
            changedTogglesState.value + (feature to value)
        } else {
            changedTogglesState.value - feature
        }

        withContext(Dispatchers.IO) {
            saveState()
        }
    }

    suspend fun resetAllToDefault() {
        changedTogglesState.value = mutableMapOf()

        withContext(Dispatchers.IO) {
            saveState()
        }
    }

    private suspend fun restoreTogglesState() {
        changedTogglesState.value = withContext(Dispatchers.IO) {
            getSavedToggles()
        }
    }

    private fun getSavedToggles(): Map<Feature, FlipperValue> {
        return try {
            ObjectInputStream(
                context.openFileInput(TOGGLES_FILE_NAME)
            ).use { inputStream ->
                val serializedToggles = (inputStream.readObject() as? Map<*, *>).orEmpty()
                val restoredFeatureToggles = mutableMapOf<Feature, FlipperValue>()

                serializedToggles.entries.forEach { (featureId, value) ->
                    val feature = defaultFeatureToggles.keys.find { it.id == featureId }
                    val defaultValue = defaultFeatureToggles[feature]
                    val restoredValue = (value as? String)?.deserializeToFlipperValue()

                    if (feature != null && defaultValue != null) {
                        restoredFeatureToggles[feature] = restoredValue ?: defaultValue
                    }
                }

                restoredFeatureToggles
            }
        } catch (e: FileNotFoundException) {
            Timber.e(e)

            mutableMapOf()
        }
    }

    private fun saveState() {
        ObjectOutputStream(
            context.openFileOutput(TOGGLES_FILE_NAME, Context.MODE_PRIVATE)
        ).use { outputStream ->
            val serializableToggles = changedTogglesState.value.entries
                .associate { (feature, value) ->
                    feature.id to value.serializeToString()
                }

            outputStream.writeObject(serializableToggles)
            outputStream.flush()
        }
    }

    private fun FlipperValue.serializeToString(): String {
        val value: String = when (this) {
            is LongValue -> this.value.toString()
            is EmptyValue -> ""
            is StringValue -> this.value
            is DoubleValue -> this.value.toString()
            is BooleanValue -> this.value.toString()
            is ByteArrayValue -> this.value.contentToString()
        }

        return "$value ${this::class.simpleName}"
    }

    private fun String.deserializeToFlipperValue(): FlipperValue? {
        val className = this.substringAfterLast(' ')
        val serializedValue = this.substringBeforeLast(' ')

        return when (className) {
            LongValue::class.simpleName -> LongValue(serializedValue.toLong())
            EmptyValue::class.simpleName -> EmptyValue
            StringValue::class.simpleName -> StringValue(serializedValue)
            DoubleValue::class.simpleName -> DoubleValue(serializedValue.toDouble())
            BooleanValue::class.simpleName -> BooleanValue(serializedValue.toBoolean())
            ByteArrayValue::class.simpleName -> ByteArrayValue(serializedValue.encodeToByteArray())

            else -> {
                Timber.e("Can't deserialize value for class: $className")

                null
            }
        }
    }
}
