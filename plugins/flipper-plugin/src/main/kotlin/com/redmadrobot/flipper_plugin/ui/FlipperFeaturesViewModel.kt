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

    init {
        updateShownFeaturesOnQueryChange()

        togglesRepository
            .getFeatureToggles()
            .onEach { pluginToggles ->
                val features = mutableListOf<FlipperFeature>()

                pluginToggles
                    .groupBy(PluginToggle::group)
                    .forEach { (groupName, toggles) ->
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
                    }

                featureItemsState.emit(features)
            }
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
        viewModelScope.launch {
            val items = featureItemsState.value
            val indexOfGroup =
                items.indexOfFirst { (it as? FlipperFeature.Group)?.name == groupName }

            if (indexOfGroup >= 0 && indexOfGroup < items.lastIndex) {
                for (i in indexOfGroup + 1..items.lastIndex) {
                    val item = (items[i] as? FlipperFeature.Item) ?: continue

                    if (item.value is FlipperValue.BooleanValue) {
                        togglesRepository.saveFeatureState(
                            item.id,
                            FlipperValue.BooleanValue(checked)
                        )
                    }

                    if (items.getOrNull(i + 1) is FlipperFeature.Group) break
                }
            }
        }
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
