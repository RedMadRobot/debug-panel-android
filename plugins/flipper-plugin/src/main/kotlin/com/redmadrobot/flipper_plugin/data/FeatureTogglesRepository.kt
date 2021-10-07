package com.redmadrobot.flipper_plugin.data

import android.content.Context
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper.config.FlipperValue.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
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
        private const val TOGGLES_FILE_NAME = "serialized_feature_toggles"
    }

    private val featureToggles = mutableMapOf<Feature, FlipperValue>()
    private val updatedFeatureToggle = MutableSharedFlow<Pair<Feature, FlipperValue>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun observeUpdatedToggle(): Flow<Pair<Feature, FlipperValue>> = updatedFeatureToggle

    suspend fun getFeatureToggles(): Map<Feature, FlipperValue> {
        if (featureToggles.isEmpty() && defaultFeatureToggles.isNotEmpty()) {
            val restoredToggles = withContext(Dispatchers.IO) {
                getSavedToggles()
            }

            featureToggles.putAll(restoredToggles)
        }

        return featureToggles
    }

    suspend fun saveFeatureState(feature: Feature, value: FlipperValue) {
        featureToggles[feature] = value

        updatedFeatureToggle.emit(feature to value)

        withContext(Dispatchers.IO) {
            saveState()
        }
    }

    suspend fun resetAllToDefault() {
        featureToggles.clear()
        featureToggles.putAll(defaultFeatureToggles)

        withContext(Dispatchers.IO) {
            saveState()
        }
    }

    private fun getSavedToggles(): MutableMap<Feature, FlipperValue> {
        return try {
            ObjectInputStream(
                context.openFileInput(TOGGLES_FILE_NAME)
            ).use { inputStream ->
                val serializedToggles = (inputStream.readObject() as? Map<String, String>).orEmpty()
                val restoredFeatureToggles = mutableMapOf<Feature, FlipperValue>()

                serializedToggles.entries.forEach { (featureId, value) ->
                    val feature = defaultFeatureToggles.keys.find { it.id == featureId }
                    val featureValue = value.deserializeToFlipperValue()
                    val defaultValue = defaultFeatureToggles[feature]

                    if (feature != null && defaultValue != null) {
                        restoredFeatureToggles[feature] = featureValue ?: defaultValue
                    }
                }

                restoredFeatureToggles
            }
        } catch (e: FileNotFoundException) {
            LinkedHashMap(defaultFeatureToggles)
        }
    }

    private fun saveState() {
        ObjectOutputStream(
            context.openFileOutput(TOGGLES_FILE_NAME, Context.MODE_PRIVATE)
        ).use { outputStream ->
            val serializableToggles = featureToggles.entries.associate { (feature, value) ->
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
