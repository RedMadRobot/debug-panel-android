package com.redmadrobot.flipper_plugin.plugin

import android.content.Context
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.data.FeatureTogglesRepository
import com.redmadrobot.flipper_plugin.ui.FlipperFeaturesViewModel

internal class FlipperPluginContainer(
    private val context: Context,
    private val defaultFeaturesMap: Map<Feature, FlipperValue>,
) : PluginDependencyContainer {

    val featureTogglesRepository by lazy(LazyThreadSafetyMode.NONE) {
        FeatureTogglesRepository(context, defaultFeaturesMap)
    }

    fun createFlipperFeaturesViewModel(): FlipperFeaturesViewModel {
        return FlipperFeaturesViewModel(featureTogglesRepository)
    }
}
