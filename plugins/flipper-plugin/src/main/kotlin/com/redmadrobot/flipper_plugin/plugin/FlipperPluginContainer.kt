package com.redmadrobot.flipper_plugin.plugin

import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.ui.FlipperFeaturesViewModel

internal class FlipperPluginContainer(
    private val featureStateMap: Map<Feature, FlipperValue>,
) : PluginDependencyContainer {

    fun createFlipperFeaturesViewModel(): FlipperFeaturesViewModel {
        return FlipperFeaturesViewModel(
            featureStateMap,
        )
    }
}
