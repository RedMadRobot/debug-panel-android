package com.redmadrobot.feature_togles_plugin.plugin

import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.feature_togles_plugin.data.LocalFeatureToggleRepository
import com.redmadrobot.feature_togles_plugin.data.repository.PluginSettingsRepository
import com.redmadrobot.feature_togles_plugin.data.storage.FeatureTogglePluginDatabase
import com.redmadrobot.feature_togles_plugin.toggles.FeatureToggleHolder
import com.redmadrobot.feature_togles_plugin.ui.FeatureTogglesViewModel

internal class FeatureTogglesPluginContainer(
    private val container: CommonContainer
) : PluginDependencyContainer {

    private val pluginStorage by lazy { FeatureTogglePluginDatabase.getInstance(container.context) }

    private val pluginSettingsRepository by lazy {
        PluginSettingsRepository(container.context)
    }

    private val localFeatureToggleRepository by lazy {
        LocalFeatureToggleRepository(
            pluginStorage.getFeatureTogglesDao(),
            pluginSettingsRepository
        )
    }

    val featureToggleHolder = FeatureToggleHolder(localFeatureToggleRepository)

    fun createFeatureTogglesViewModel(): FeatureTogglesViewModel {
        return FeatureTogglesViewModel(
            localFeatureToggleRepository,
            pluginSettingsRepository
        )
    }
}
