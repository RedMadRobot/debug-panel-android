package com.redmadrobot.debug_panel.internal.plugin.app_settings

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel.data.settings.DefaultSharedPreferences
import com.redmadrobot.debug_panel.internal.CommonContainer
import com.redmadrobot.debug_panel.internal.plugin.Plugin
import com.redmadrobot.debug_panel.internal.plugin.PluginDependencyContainer
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
