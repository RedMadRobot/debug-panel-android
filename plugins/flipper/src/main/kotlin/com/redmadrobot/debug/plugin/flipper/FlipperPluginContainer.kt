package com.redmadrobot.debug.plugin.flipper

import android.content.Context
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.flipper.data.FeatureTogglesRepository
import com.redmadrobot.debug.plugin.flipper.ui.FlipperFeaturesViewModel
import com.redmadrobot.debug.plugin.flipper.ui.dialog.SourceSelectionViewModel

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
