package com.redmadrobot.feature_togles_plugin.plugin

import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.plugin.PluginDependencyContainer
import com.redmadrobot.feature_togles_plugin.data.LocalFeatureToggleRepository
import com.redmadrobot.feature_togles_plugin.toggles.FeatureToggleHolder
import com.redmadrobot.feature_togles_plugin.ui.FeatureTogglesViewModel

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
