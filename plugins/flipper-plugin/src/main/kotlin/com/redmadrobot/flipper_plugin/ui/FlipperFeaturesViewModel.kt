package com.redmadrobot.flipper_plugin.ui

import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.data.FeatureTogglesRepository
import com.redmadrobot.flipper_plugin.ui.item.FlipperFeatureItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class FlipperFeaturesViewModel(
    private val togglesRepository: FeatureTogglesRepository,
) : PluginViewModel() {

    private val _state = MutableStateFlow(FlipperFeaturesViewState())
    val state = _state.asStateFlow()

    private val queryState = MutableStateFlow("")
    private val featureItemsState = MutableStateFlow(emptyList<FlipperFeatureItem>())

    init {
        updateAvailableFeatures()

        updateShownFeaturesOnQueryChange()
    }

    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            queryState.emit(query)
        }
    }

    fun onFeatureValueChanged(feature: Feature, value: FlipperValue) {
        viewModelScope.launch {
            togglesRepository.saveFeatureState(feature, value)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val updatedFeatureItemsState = featureItemsState.value.map { item ->
                if (item.feature == feature) {
                    item.copy(value = value)
                } else {
                    item
                }
            }

            featureItemsState.emit(updatedFeatureItemsState)
        }
    }

    fun onResetClicked() {
        viewModelScope.launch {
            togglesRepository.resetAllToDefault()

            updateAvailableFeatures()
        }
    }

    private fun updateAvailableFeatures() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = togglesRepository.getFeatureToggles().map { (feature, value) ->
                FlipperFeatureItem(feature, value)
            }

            featureItemsState.emit(items)
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
                featureItems.filter { query in it.feature.id }
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
