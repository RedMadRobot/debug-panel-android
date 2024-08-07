package com.redmadrobot.debug.plugin.konfeature.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.core.internal.PluginViewModel
import com.redmadrobot.debug.plugin.konfeature.KonfeatureDebugPanelInterceptor
import com.redmadrobot.debug.plugin.konfeature.ui.data.KonfeatureItem
import com.redmadrobot.debug.plugin.konfeature.ui.data.KonfeatureViewState
import com.redmadrobot.konfeature.Konfeature
import com.redmadrobot.konfeature.source.FeatureValueSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class KonfeatureViewModel(
    private val konfeature: Konfeature,
    private val debugPanelInterceptor: KonfeatureDebugPanelInterceptor,
) : PluginViewModel() {

    private val _state = MutableStateFlow(KonfeatureViewState())

    val state: Flow<KonfeatureViewState> = _state.asStateFlow()

    init {
        debugPanelInterceptor
            .valuesFlow
            .onEach {
                val items = withContext(Dispatchers.Default) { konfeature.getItems() }
                _state.update { it.copy(items = items) }
            }
            .launchIn(viewModelScope)
    }

    fun onValueChanged(key: String, value: Any) {
        viewModelScope.launch {
            debugPanelInterceptor.setValue(key, value)
        }
    }

    fun onValueReset(key: String) {
        viewModelScope.launch {
            debugPanelInterceptor.resetValue(key)
        }
    }

    fun onHeaderClicked(configName: String) {
        _state.update {
            val newCollapsedConfigs = if (configName in it.collapsedConfigs) {
                it.collapsedConfigs - configName
            } else {
                it.collapsedConfigs + configName
            }
            it.copy(collapsedConfigs = newCollapsedConfigs)
        }
    }

    fun onRefreshClicked() {
        viewModelScope.launch {
            val items = withContext(Dispatchers.Default) { konfeature.getItems() }
            _state.update { it.copy(items = items) }
        }
    }

    fun onResetClicked() {
        viewModelScope.launch {
            debugPanelInterceptor.resetAll()
        }
    }

    fun onCollapseAllClicked() {
        _state.update {
            val collapsedConfigs = it.items
                .asSequence()
                .filterIsInstance(KonfeatureItem.Config::class.java)
                .map { it.name }
                .toSet()
            it.copy(collapsedConfigs = collapsedConfigs)
        }
    }

    private fun Konfeature.getItems(): List<KonfeatureItem> {
        val items = mutableListOf<KonfeatureItem>()

        spec.forEach { config ->
            items.add(
                KonfeatureItem.Config(
                    name = config.name,
                    description = config.description
                )
            )

            config.values.forEach { value ->
                val configValue = getValue(value)

                val sourceColor = when (configValue.source) {
                    FeatureValueSource.Default -> Color.Gray
                    is FeatureValueSource.Interceptor -> Color.Red
                    is FeatureValueSource.Source -> Color.Green
                }

                items.add(
                    KonfeatureItem.Value(
                        key = value.key,
                        value = configValue.value,
                        configName = config.name,
                        sourceName = getSourceName(configValue.source),
                        sourceColor = sourceColor,
                        description = value.description,
                        isDebugSource = configValue.source.isDebugSource(),
                    )
                )
            }
        }

        return items
    }

    private fun FeatureValueSource.isDebugSource(): Boolean {
        return (this as? FeatureValueSource.Interceptor)?.name == debugPanelInterceptor.name
    }

    private fun getSourceName(source: FeatureValueSource): String {
        return when (source) {
            FeatureValueSource.Default -> "Default"
            is FeatureValueSource.Interceptor -> source.name
            is FeatureValueSource.Source -> source.name
            else -> "Unknown"
        }
    }
}


