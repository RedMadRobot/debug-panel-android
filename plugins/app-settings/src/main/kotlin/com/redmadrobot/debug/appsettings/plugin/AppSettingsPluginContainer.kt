package com.redmadrobot.debug.appsettings.plugin

import android.content.SharedPreferences
import com.redmadrobot.debug.appsettings.data.AppSettingsRepositoryImpl
import com.redmadrobot.debug.appsettings.ui.ApplicationSettingsViewModel
import com.redmadrobot.debug.core.plugin.PluginDependencyContainer

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
