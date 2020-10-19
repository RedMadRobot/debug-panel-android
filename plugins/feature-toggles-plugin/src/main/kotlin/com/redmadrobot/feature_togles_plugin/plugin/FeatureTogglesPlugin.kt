package com.redmadrobot.feature_togles_plugin.plugin

import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.feature_togles_plugin.toggles.FeatureTogglesConfig
import com.redmadrobot.feature_togles_plugin.ui.FeatureTogglesFragment

class FeatureTogglesPlugin(
    private val featureTogglesConfig: FeatureTogglesConfig? = null
) : Plugin() {

    companion object {
        const val NAME = "FEATURE TOGGLE"
    }

    override fun getName() = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return FeatureTogglesPluginContainer(commonContainer).apply {
            featureTogglesConfig?.let(featureToggleHolder::initConfig)
        }
    }

    override fun getFragment(): Fragment? {
        return FeatureTogglesFragment()
    }
}
