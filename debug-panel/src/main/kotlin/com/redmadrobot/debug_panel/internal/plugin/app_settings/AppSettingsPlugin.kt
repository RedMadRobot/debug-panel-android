package com.redmadrobot.debug_panel.internal.plugin.app_settings

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.Plugin
import com.redmadrobot.core.PluginDependencyContainer
import com.redmadrobot.core.data.settings.DefaultSharedPreferences
import com.redmadrobot.debug_panel.ui.settings.ApplicationSettingsFragment

class AppSettingsPlugin(
    private val sharedPreferences: List<SharedPreferences> = listOf(DefaultSharedPreferences())
) : Plugin() {

    companion object {
        const val NAME = "APP_SETTINGS"
    }

    override fun getName() = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return AppSettingsPluginContainer(sharedPreferences, commonContainer)
    }

    override fun getFragment(): Fragment? {
        return ApplicationSettingsFragment()
    }
}
