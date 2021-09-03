package com.redmadrobot.flipper_plugin.ui

import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.plugin.FeatureValueChangedEvent
import com.redmadrobot.flipper_plugin.plugin.FlipperPlugin
import com.redmadrobot.flipper_plugin.ui.item.FlipperFeatureItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class FlipperFeaturesViewModel(
    private val featureValueMap: Map<Feature, FlipperValue>,
) : PluginViewModel() {

    private val _state = MutableStateFlow(FlipperFeaturesViewState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val initialState = FlipperFeaturesViewState(
                featureItems = featureValueMap.map { (feature, value) ->
                    FlipperFeatureItem(feature, value)
                }
            )

            _state.emit(initialState)
        }
    }

    fun onFeatureValueChanged(feature: Feature, value: FlipperValue) {
        viewModelScope.launch {
            _state.emit(
                state.value.copy(
                    featureItems = state.value.featureItems.map { item ->
                        if (item.feature == feature) {
                            item.copy(value = value)
                        } else {
                            item
                        }
                    }
                )
            )
        }

        getPlugin<FlipperPlugin>().pushEvent(
            FeatureValueChangedEvent(feature, value)
        )
    }
}
