package com.redmadrobot.flipper_plugin.ui

import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.plugin.FeatureValueChangedEvent
import com.redmadrobot.flipper_plugin.plugin.FlipperPlugin
import com.redmadrobot.flipper_plugin.ui.item.FlipperFeatureItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class FlipperFeaturesViewModel(
    private val featureValueMap: Map<Feature, FlipperValue>,
) : PluginViewModel() {

    private val _state = MutableStateFlow(FlipperFeaturesViewState())
    val state = _state.asStateFlow()

    private val queryState = MutableStateFlow("")
    private val featureItemsState = MutableStateFlow(
        featureValueMap.map { (feature, value) ->
            FlipperFeatureItem(feature, value)
        }
    )

    init {
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
            .launchIn(viewModelScope)
    }

    fun onQueryChanged(query: String) {
        viewModelScope.launch {
            queryState.emit(query)
        }
    }

    fun onFeatureValueChanged(feature: Feature, value: FlipperValue) {
        viewModelScope.launch {
            featureItemsState.emit(
                featureItemsState.value.map { item ->
                    if (item.feature == feature) {
                        item.copy(value = value)
                    } else {
                        item
                    }
                }
            )
        }

        getPlugin<FlipperPlugin>().pushEvent(
            FeatureValueChangedEvent(feature, value)
        )
    }

    internal companion object {
        private const val QUERY_DEBOUNCE_MS = 450L
    }
}
