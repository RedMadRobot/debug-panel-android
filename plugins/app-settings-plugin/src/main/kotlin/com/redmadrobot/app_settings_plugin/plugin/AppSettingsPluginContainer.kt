package com.redmadrobot.app_settings_plugin.plugin

import android.content.SharedPreferences
import com.redmadrobot.app_settings_plugin.data.AppSettingsRepositoryImpl
import com.redmadrobot.app_settings_plugin.ui.ApplicationSettingsViewModel
import com.redmadrobot.core.plugin.PluginDependencyContainer

internal class AppSettingsPluginContainer(
    private val preferencesList: List<SharedPreferences>
) : PluginDependencyContainer {

    private val appSettingsRepository by lazy {
        AppSettingsRepositoryImpl(preferencesList)
    }

    fun createApplicationSettingsViewModel(): ApplicationSettingsViewModel {
        return ApplicationSettingsViewModel(appSettingsRepository)
    }
}
