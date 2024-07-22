package com.redmadrobot.debug.plugin.appsettings

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.plugin.appsettings.data.DefaultSharedPreferences
import com.redmadrobot.debug.plugin.appsettings.ui.ApplicationSettingsFragment
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.internal.PluginDependencyContainer

public class AppSettingsPlugin(
    private val sharedPreferences: List<SharedPreferences> = listOf(DefaultSharedPreferences())
) : Plugin() {

    internal companion object {
        const val NAME = "APP SETTINGS"
    }

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return AppSettingsPluginContainer(sharedPreferences)
    }

    override fun getFragment(): Fragment? {
        return ApplicationSettingsFragment()
    }
}
