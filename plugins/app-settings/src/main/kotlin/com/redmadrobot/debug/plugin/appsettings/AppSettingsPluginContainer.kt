package com.redmadrobot.debug.plugin.appsettings

import android.content.SharedPreferences
import com.redmadrobot.debug.plugin.appsettings.data.AppSettingsRepositoryImpl
import com.redmadrobot.debug.plugin.appsettings.ui.ApplicationSettingsViewModel
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
