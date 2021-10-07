package com.redmadrobot.flipper_plugin.plugin

import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.ui.FlipperFeaturesFragment

public class FlipperPlugin(
    private val featureStateMap: Map<Feature, FlipperValue>,
) : Plugin() {

    internal companion object {
        const val NAME = "FLIPPER PLUGIN"
    }

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return FlipperPluginContainer(
            context = commonContainer.context,
            defaultFeaturesMap = featureStateMap,
        )
    }

    override fun getFragment(): Fragment {
        return FlipperFeaturesFragment()
    }
}
