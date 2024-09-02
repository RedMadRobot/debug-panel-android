package com.redmadrobot.debug.plugin.flipper.ui

import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.core.internal.PluginViewModel
import com.redmadrobot.debug.plugin.flipper.PluginToggle
import com.redmadrobot.debug.plugin.flipper.data.FeatureTogglesRepository
import com.redmadrobot.debug.plugin.flipper.ui.data.FlipperItem
import com.redmadrobot.flipper.config.FlipperValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class FlipperFeaturesViewModel(
    private val togglesRepository: FeatureTogglesRepository,
) : PluginViewModel() {

    private val _state = MutableStateFlow(FlipperFeaturesViewState())
    val state = _state.asStateFlow()

    private val queryState = MutableStateFlow("")
    private val featureItemsState = MutableStateFlow(emptyList<FlipperItem>())
    private val groupedFeaturesState = MutableStateFlow(emptyMap<String, List<FlipperItem>>())
    private val collapsedGroupsState = MutableStateFlow(emptySet<String>())

    init {
        updateShownFeaturesOnQueryChange()

        upkeepFeatureGroups()
        upkeepFeatureItems()
    }

    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            queryState.emit(query)
        }
    }

    fun onFeatureValueChanged(feature: String, value: FlipperValue) {
        viewModelScope.launch {
            togglesRepository.saveFeatureState(feature, value)
        }
    }

    fun onGroupToggleStateChanged(groupName: String, checked: Boolean) {
        val items = groupedFeaturesState.value[groupName] ?: return
        viewModelScope.launch {
            for (item in items) {
                item as? FlipperItem.Feature ?: continue

                if (item.value is FlipperValue.BooleanValue) {
                    togglesRepository.saveFeatureState(
                        item.id,
                        FlipperValue.BooleanValue(checked)
                    )
                }
            }
        }
    }

    fun onGroupClick(groupName: String) {
        val collapsedGroups = collapsedGroupsState.value

        collapsedGroupsState.tryEmit(
            if (groupName in collapsedGroups) {
                collapsedGroups - groupName
            } else {
                collapsedGroups + groupName
            }
        )
    }

    fun onResetClicked() {
        viewModelScope.launch {
            togglesRepository.resetAllToDefault()
        }
    }

    private fun upkeepFeatureGroups() {
        combine(
            togglesRepository.getSources(),
            togglesRepository.getSelectedSource(),
        ) { sources, selectedSource ->
            sources.getValue(selectedSource)
        }
            .flatMapLatest { it }
            .map { pluginToggles ->
                pluginToggles.groupByGroupName()
            }
            .onEach(groupedFeaturesState::emit)
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    private fun upkeepFeatureItems() {
        combine(
            groupedFeaturesState,
            collapsedGroupsState,
        ) { features, collapsedGroups ->
            features
                .flatMap { (groupName, items) ->
                    if (groupName in collapsedGroups) {
                        listOf(items.first())
                    } else {
                        items
                    }
                }
        }
            .onEach(featureItemsState::emit)
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    // Функция отвечает за обновление списка фичей с учётом ввода из поисковой строки
    private fun updateShownFeaturesOnQueryChange() {
        combine(
            queryState.debounce(QUERY_DEBOUNCE_MS).distinctUntilChanged(),
            featureItemsState,
        ) { query, featureItems ->
            if (query.isBlank()) {
                featureItems
            } else {
                featureItems.filter { flipperFeature ->
                    when (flipperFeature) {
                        is FlipperItem.Feature -> {
                            flipperFeature.description.contains(query, ignoreCase = true)
                        }

                        is FlipperItem.Group -> {
                            flipperFeature.name.contains(query, ignoreCase = true)
                        }
                    }
                }
            }
        }
            .onEach { featureItems ->
                _state.emit(FlipperFeaturesViewState(featureItems))
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun List<PluginToggle>.groupByGroupName(): MutableMap<String, List<FlipperItem>> {
        val mappedGroups = mutableMapOf<String, List<FlipperItem>>()

        this
            .groupBy(PluginToggle::group)
            .forEach { (groupName, toggles) ->
                val features = mutableListOf<FlipperItem>()

                features += FlipperItem.Group(
                    name = groupName,
                    editable = toggles.all(PluginToggle::editable),
                    allEnabled = toggles.all { toggle ->
                        (toggle.value as? FlipperValue.BooleanValue)?.value ?: true
                    },
                )

                toggles.forEach { toggle ->
                    features += FlipperItem.Feature(
                        id = toggle.id,
                        value = toggle.value,
                        editable = toggle.editable,
                        description = toggle.description,
                    )
                }

                mappedGroups[groupName] = features
            }

        return mappedGroups
    }

    internal companion object {
        private const val QUERY_DEBOUNCE_MS = 450L
    }
}

internal data class FlipperFeaturesViewState(val items: List<FlipperItem> = emptyList())
