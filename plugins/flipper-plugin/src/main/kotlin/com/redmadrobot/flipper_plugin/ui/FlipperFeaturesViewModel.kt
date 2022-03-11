package com.redmadrobot.flipper_plugin.ui

import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.data.FeatureTogglesRepository
import com.redmadrobot.flipper_plugin.plugin.PluginToggle
import com.redmadrobot.flipper_plugin.ui.data.FlipperFeature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class FlipperFeaturesViewModel(
    private val togglesRepository: FeatureTogglesRepository,
) : PluginViewModel() {

    private val _state = MutableStateFlow(FlipperFeaturesViewState())
    val state = _state.asStateFlow()

    private val queryState = MutableStateFlow("")
    private val featureItemsState = MutableStateFlow(emptyList<FlipperFeature>())
    private val groupedFeaturesState = MutableStateFlow(emptyMap<String, List<FlipperFeature>>())
    private val collapsedGroupsState = MutableStateFlow(emptySet<String>())

    init {
        updateShownFeaturesOnQueryChange()

        togglesRepository
            .getFeatureToggles()
            .map { pluginToggles ->
                val mappedGroups = mutableMapOf<String, List<FlipperFeature>>()

                pluginToggles
                    .groupBy(PluginToggle::group)
                    .forEach { (groupName, toggles) ->
                        val features = mutableListOf<FlipperFeature>()

                        features += FlipperFeature.Group(
                            name = groupName,
                            allEnabled = toggles.all { toggle ->
                                (toggle.value as? FlipperValue.BooleanValue)?.value ?: true
                            }
                        )

                        toggles.forEach { toggle ->
                            features += FlipperFeature.Item(
                                id = toggle.id,
                                value = toggle.value,
                                description = toggle.description,
                            )
                        }

                        mappedGroups[groupName] = features
                    }

                mappedGroups
            }
            .onEach(groupedFeaturesState::emit)
            .flowOn(Dispatchers.Main)
            .launchIn(viewModelScope)

        combine(
            groupedFeaturesState,
            collapsedGroupsState,
        ) { features, collapsedGroups ->
            features
                .map { (groupName, items) ->
                    if (groupName in collapsedGroups) {
                        listOf(items.first())
                    } else {
                        items
                    }
                }
                .flatten()
        }
            .onEach(featureItemsState::emit)
            .flowOn(Dispatchers.Main)
            .launchIn(viewModelScope)
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
                item as? FlipperFeature.Item ?: continue

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
                        is FlipperFeature.Item -> {
                            flipperFeature.description.contains(query, ignoreCase = true)
                        }

                        is FlipperFeature.Group -> {
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

    internal companion object {
        private const val QUERY_DEBOUNCE_MS = 450L
    }
}
