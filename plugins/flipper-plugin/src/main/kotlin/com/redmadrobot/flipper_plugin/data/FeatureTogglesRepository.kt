package com.redmadrobot.flipper_plugin.data

import android.content.Context
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper.config.FlipperValue.*
import com.redmadrobot.flipper_plugin.plugin.PluginToggle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

internal class FeatureTogglesRepository(
    private val context: Context,
    private val defaultToggles: List<PluginToggle>,
) {
    companion object {
        private const val TOGGLES_FILE_NAME = "com.redmadrobot.flipper_plugin.toggles"
    }

    private val defaultTogglesState = MutableStateFlow(defaultToggles)
    private val changedTogglesState = MutableStateFlow(emptyMap<String, FlipperValue>())

    private val defaultSource = FeatureTogglesSource("Default")
    private val defaultFeatureToggles = defaultToggles.associate { it.id to it.value }

    private val sources = MutableStateFlow(emptyMap<FeatureTogglesSource, Flow<List<PluginToggle>>>())
    private val selectedSource = MutableStateFlow<FeatureTogglesSource>(defaultSource)

    init {
        CoroutineScope(Dispatchers.IO).launch { restoreTogglesState() }

        sources.tryEmit(mapOf(defaultSource to getChangeableFeatureToggles()))
        selectedSource.tryEmit(defaultSource)
    }

    fun observeChangedToggles(): Flow<Map<String, FlipperValue>> {
        return selectedSource
            .flatMapLatest { selectedSource ->
                sources.value[selectedSource]
                    ?.map { toggles ->
                        toggles.fold(defaultFeatureToggles.toMutableMap()) { acc, toggle ->
                            acc[toggle.id] = toggle.value
                            acc
                        }
                    }
                    ?: emptyFlow()
            }
    }

    private fun getChangeableFeatureToggles(): Flow<List<PluginToggle>> {
        return combine(
            defaultTogglesState,
            changedTogglesState,
        ) { defaultToggles, changedToggles ->
            if (changedToggles.isEmpty()) {
                defaultToggles
            } else {
                defaultToggles.map { toggle ->
                    changedToggles[toggle.id]?.let { changedValue ->
                        toggle.copy(value = changedValue)
                    } ?: toggle
                }
            }
        }
            .flowOn(Dispatchers.Main)
    }

    fun addSource(sourceName: String, toggles: Map<String, FlipperValue>) {
        require(sourceName.isNotBlank())

        val togglesFlow = defaultTogglesState.map { defaultToggles ->
            defaultToggles.mapNotNull { defaultToggle ->
                if (defaultToggle.id in toggles) {
                    defaultToggle.copy(
                        value = toggles[defaultToggle.id]!!,
                        editable = false,
                    )
                } else {
                    null
                }
            }
        }

        val updatedSources = sources.value + (FeatureTogglesSource(sourceName) to togglesFlow)

        sources.tryEmit(updatedSources)
    }

    fun setSelectedSource(source: FeatureTogglesSource) {
        selectedSource.tryEmit(source)
    }

    fun getSources(): Flow<Map<FeatureTogglesSource, Flow<List<PluginToggle>>>> {
        return sources
    }

    fun getSelectedSource(): Flow<FeatureTogglesSource> {
        return selectedSource
    }

    suspend fun saveFeatureState(featureId: String, value: FlipperValue) {
        changedTogglesState.value = if (defaultFeatureToggles[featureId] != value) {
            changedTogglesState.value + (featureId to value)
        } else {
            changedTogglesState.value - featureId
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

    private fun getSavedToggles(): Map<String, FlipperValue> {
        if (context.getFileStreamPath(TOGGLES_FILE_NAME)?.exists() != true) {
            return emptyMap()
        }

        return try {
            ObjectInputStream(
                context.openFileInput(TOGGLES_FILE_NAME)
            ).use { inputStream ->
                val serializedToggles = (inputStream.readObject() as? Map<*, *>).orEmpty()
                val restoredFeatureToggles = mutableMapOf<String, FlipperValue>()

                serializedToggles.entries.forEach { (serializedFeatureId, value) ->
                    val featureId = serializedFeatureId as? String
                    val defaultValue = defaultFeatureToggles[featureId]
                    val restoredValue = (value as? String)?.deserializeToFlipperValue()

                    if (featureId != null &&
                        featureId in defaultFeatureToggles.keys &&
                        defaultValue != null
                    ) {
                        restoredFeatureToggles[featureId] = restoredValue ?: defaultValue
                    }
                }

                restoredFeatureToggles
            }
        } catch (e: FileNotFoundException) {
            Timber.e(e)

            emptyMap()
        }
    }

    private fun saveState() {
        ObjectOutputStream(
            context.openFileOutput(TOGGLES_FILE_NAME, Context.MODE_PRIVATE)
        ).use { outputStream ->
            val serializableToggles = changedTogglesState.value.entries
                .associate { (featureId, value) ->
                    featureId to value.serializeToString()
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

        return "${this::class.simpleName} $value"
    }

    private fun String.deserializeToFlipperValue(): FlipperValue? {
        val (className, serializedValue) = this.split(' ', limit = 2)

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
