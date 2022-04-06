package com.redmadrobot.flipper_plugin.plugin

import android.content.Context
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.flipper_plugin.data.FeatureTogglesRepository
import com.redmadrobot.flipper_plugin.ui.FlipperFeaturesViewModel
import com.redmadrobot.flipper_plugin.ui.dialog.SourceSelectionViewModel

internal class FlipperPluginContainer(
    private val context: Context,
    private val defaultToggles: List<PluginToggle>,
) : PluginDependencyContainer {

    val featureTogglesRepository by lazy(LazyThreadSafetyMode.NONE) {
        FeatureTogglesRepository(context, defaultToggles)
    }

    fun createFlipperFeaturesViewModel(): FlipperFeaturesViewModel {
        return FlipperFeaturesViewModel(featureTogglesRepository)
    }

    fun createFlipperSourceSelectionViewModel(): SourceSelectionViewModel {
        return SourceSelectionViewModel(context, featureTogglesRepository)
    }
}
