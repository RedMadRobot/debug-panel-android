package com.redmadrobot.flipper_plugin.ui

import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.ui.item.FlipperFeatureItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal class FlipperFeaturesViewModel(
    private val featureStateMap: Map<Feature, FlipperValue>,
) : PluginViewModel() {

    private val _state = MutableSharedFlow<FlipperFeaturesViewState>()
    val state = _state as SharedFlow<FlipperFeaturesViewState>

    init {
        val initialState = FlipperFeaturesViewState(
            featureItems = featureStateMap.map { (feature, value) ->
                FlipperFeatureItem(feature.id, value)
            }
        )

        _state.tryEmit(initialState)
    }

    fun onBooleanFeatureStateChanged(featureName: CharSequence, state: Boolean) {

    }
}
