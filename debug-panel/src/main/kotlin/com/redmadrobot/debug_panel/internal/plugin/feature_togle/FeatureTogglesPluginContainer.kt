package com.redmadrobot.debug_panel.internal.plugin.feature_togle

import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.plugin.PluginDependencyContainer
import com.redmadrobot.debug_panel.data.toggles.LocalFeatureToggleRepository
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleHolder
import com.redmadrobot.debug_panel.ui.toggles.FeatureTogglesViewModel

internal class FeatureTogglesPluginContainer(
    private val container: CommonContainer
) : PluginDependencyContainer {

    private val localFeatureToggleRepository by lazy {
        LocalFeatureToggleRepository(
            container.dataBaseInstance.getFeatureTogglesDao(),
            container.panelSettingsRepository
        )
    }

    val featureToggleHolder = FeatureToggleHolder(localFeatureToggleRepository)

    fun createFeatureTogglesViewModel(): FeatureTogglesViewModel {
        return FeatureTogglesViewModel(
            localFeatureToggleRepository,
            container.panelSettingsRepository
        )
    }
}
