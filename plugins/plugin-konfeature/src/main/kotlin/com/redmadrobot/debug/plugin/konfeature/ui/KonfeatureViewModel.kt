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
            .onEach { updateItems() }
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
        _state.update { state ->
            val collapsedConfigs = state.items
                .asSequence()
                .filterIsInstance(KonfeatureItem.Config::class.java)
                .map { it.name }
                .toSet()
            state.copy(collapsedConfigs = collapsedConfigs)
        }
    }

    fun onEditClick(key: String, value: Any, isDebugSource: Boolean) {
        _state.update { it.copy(editDialogState = EditDialogState(key, value, isDebugSource)) }
    }

    fun onEditDialogCloseClicked() {
        _state.update { it.copy(editDialogState = null) }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { state ->
            val filteredItems = filterItems(state.items, query)
            state.copy(searchQuery = query, filteredItems = filteredItems)
        }
    }

    private suspend fun updateItems() {
        val items = withContext(Dispatchers.IO) { getItems(konfeature) }
        _state.update { state ->
            val filteredItems = filterItems(items, state.searchQuery)
            state.copy(items = items, filteredItems = filteredItems)
        }
    }

    private fun getItems(konfeature: Konfeature): List<KonfeatureItem> {
        return konfeature.spec.fold(mutableListOf<KonfeatureItem>()) { acc, configSpec ->
            acc.apply {
                add(createConfigItem(configSpec))
                addAll(configSpec.values.map { valueSpec ->
                    createConfigValueItem(
                        configName = configSpec.name,
                        valueSpec = valueSpec,
                        konfeature = konfeature
                    )
                })
            }
        }
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

    private fun filterItems(items: List<KonfeatureItem>, query: String): List<KonfeatureItem> {
        if (query.isBlank()) return items

        val formattedQuery = query.lowercase()
        val matchingItems = items
            .filterIsInstance<KonfeatureItem.Value>()
            .filter { it.key.lowercase().contains(formattedQuery) }
        val matchingConfigNames = matchingItems.map { it.configName }.toSet()

        return items.filter { item ->
            when (item) {
                is KonfeatureItem.Config -> item.name in matchingConfigNames
                is KonfeatureItem.Value -> item.key.lowercase().contains(formattedQuery)
            }
        }
    }
}


