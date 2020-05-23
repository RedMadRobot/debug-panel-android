package com.redmadrobot.debug_panel.internal.plugin.app_settings

import android.content.SharedPreferences
import com.redmadrobot.debug_panel.data.settings.AppSettingsRepositoryImpl
import com.redmadrobot.debug_panel.internal.CommonContainer
import com.redmadrobot.debug_panel.internal.plugin.PluginDependencyContainer
import com.redmadrobot.debug_panel.ui.settings.ApplicationSettingsViewModel

internal class AppSettingsPluginContainer(
    private val preferencesList: List<SharedPreferences>,
    private val container: CommonContainer
) : PluginDependencyContainer {

    private val appSettingsRepository by lazy {
        AppSettingsRepositoryImpl(preferencesList)
    }

    fun createApplicationSettingsViewModel(): ApplicationSettingsViewModel {
        return ApplicationSettingsViewModel(appSettingsRepository)
    }
}
