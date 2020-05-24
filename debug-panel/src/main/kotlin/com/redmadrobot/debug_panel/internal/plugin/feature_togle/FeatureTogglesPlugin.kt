package com.redmadrobot.debug_panel.internal.plugin.feature_togle

import androidx.fragment.app.Fragment
import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.plugin.Plugin
import com.redmadrobot.core.plugin.PluginDependencyContainer
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig
import com.redmadrobot.debug_panel.ui.toggles.FeatureTogglesFragment

class FeatureTogglesPlugin(
    private val featureTogglesConfig: FeatureTogglesConfig? = null
) : Plugin() {

    companion object {
        const val NAME = "FEATURE_TOGGLE"
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
