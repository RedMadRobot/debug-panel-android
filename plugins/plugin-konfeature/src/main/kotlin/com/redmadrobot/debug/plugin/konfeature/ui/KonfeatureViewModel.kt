package com.redmadrobot.debug.plugin.konfeature.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.core.internal.PluginViewModel
import com.redmadrobot.debug.plugin.konfeature.KonfeatureDebugPanelInterceptor
import com.redmadrobot.debug.plugin.konfeature.ui.data.EditDialogState
import com.redmadrobot.debug.plugin.konfeature.ui.data.KonfeatureItem
import com.redmadrobot.debug.plugin.konfeature.ui.data.KonfeatureViewState
import com.redmadrobot.konfeature.FeatureConfigSpec
import com.redmadrobot.konfeature.FeatureValueSpec
import com.redmadrobot.konfeature.Konfeature
import com.redmadrobot.konfeature.source.FeatureValueSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val SEARCH_QUERY_DELAY_MILLIS = 500L

internal class KonfeatureViewModel(
    private val konfeature: Konfeature,
    private val debugPanelInterceptor: KonfeatureDebugPanelInterceptor,
) : PluginViewModel() {
    private val _state = MutableStateFlow(KonfeatureViewState())
    private val _searchQueryFlow = MutableStateFlow("")

    val state: Flow<KonfeatureViewState> = _state.asStateFlow()

    init {
        observeKonfeatureValues()
        observeSearchQuery()
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

    fun onConfigHeaderClick(configName: String) {
        _state.update { state ->
            val newCollapsedConfigs = if (configName in state.collapsedConfigs) {
                state.collapsedConfigs - configName
            } else {
                state.collapsedConfigs + configName
            }
            state.copy(collapsedConfigs = newCollapsedConfigs)
        }
    }

    fun onRefreshClick() {
        viewModelScope.launch { updateItems() }
    }

    fun onResetAllClick() {
        viewModelScope.launch {
            debugPanelInterceptor.resetAllValues()
        }
    }

    fun onCollapseAllClick() {
        _state.update { state -> state.copy(collapsedConfigs = state.configs.keys) }
    }

    fun onEditClick(key: String, value: Any, isDebugSource: Boolean) {
        _state.update { it.copy(editDialogState = EditDialogState(key, value, isDebugSource)) }
    }

    fun onEditDialogCloseClicked() {
        _state.update { it.copy(editDialogState = null) }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { state -> state.copy(searchQuery = query) }
        _searchQueryFlow.update { query }
    }

    private fun observeKonfeatureValues() {
        debugPanelInterceptor.valuesFlow
            .onEach { updateItems() }
            .launchIn(viewModelScope)
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        _searchQueryFlow
            .debounce(timeoutMillis = SEARCH_QUERY_DELAY_MILLIS)
            .onEach { query ->
                _state.update { state ->
                    state.copy(filteredItems = filterItems(state.configs, state.values, query))
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun updateItems() {
        val (configs, values) = withContext(Dispatchers.IO) { getItems(konfeature) }
        val searchQuery = _searchQueryFlow.value
        val filteredItems = filterItems(configs, values, searchQuery)

        _state.update { state ->
            state.copy(configs = configs, values = values, filteredItems = filteredItems)
        }
    }

    private fun getItems(konfeature: Konfeature): Pair<Map<String, KonfeatureItem.Config>, List<KonfeatureItem.Value>> {
        val configs = mutableMapOf<String, KonfeatureItem.Config>()
        val values = mutableListOf<KonfeatureItem.Value>()

        konfeature.spec.fold(configs to values) { acc, configSpec ->
            acc.apply {
                configs[configSpec.name] = createConfigItem(configSpec)
                configSpec.values.mapTo(values) { valueSpec ->
                    createConfigValueItem(
                        configName = configSpec.name,
                        valueSpec = valueSpec,
                        konfeature = konfeature
                    )
                }
            }
        }

        return configs to values
    }

    private fun createConfigItem(config: FeatureConfigSpec): KonfeatureItem.Config {
        return KonfeatureItem.Config(
            name = config.name,
            description = config.description
        )
    }

    private fun createConfigValueItem(
        configName: String,
        valueSpec: FeatureValueSpec<out Any>,
        konfeature: Konfeature,
    ): KonfeatureItem.Value {
        val configValue = konfeature.getValue(valueSpec)

        val sourceColor = when (configValue.source) {
            FeatureValueSource.Default -> Color.Gray
            is FeatureValueSource.Interceptor -> Color.Red
            is FeatureValueSource.Source -> Color.Green
        }

        return KonfeatureItem.Value(
            key = valueSpec.key,
            value = configValue.value,
            configName = configName,
            sourceName = getSourceName(configValue.source),
            sourceColor = sourceColor,
            description = valueSpec.description,
            isDebugSource = isDebugSource(configValue.source),
        )
    }

    private fun isDebugSource(source: FeatureValueSource): Boolean {
        return (source as? FeatureValueSource.Interceptor)?.name == debugPanelInterceptor.name
    }

    private fun getSourceName(source: FeatureValueSource): String {
        return when (source) {
            FeatureValueSource.Default -> "Default"
            is FeatureValueSource.Interceptor -> source.name
            is FeatureValueSource.Source -> source.name
            else -> "Unknown"
        }
    }

    private suspend fun filterItems(
        configs: Map<String, KonfeatureItem.Config>,
        values: List<KonfeatureItem.Value>,
        query: String
    ): List<KonfeatureItem> {
        return withContext(Dispatchers.Default) {
            buildList {
                var previousValue: KonfeatureItem.Value? = null

                for (value in values) {
                    if (value.key.contains(query, ignoreCase = true)) {
                        if (previousValue?.configName != value.configName) {
                            configs[value.configName]?.let { config -> add(config) }
                        }
                        add(value)
                        previousValue = value
                    }
                }
            }
        }
    }
}


