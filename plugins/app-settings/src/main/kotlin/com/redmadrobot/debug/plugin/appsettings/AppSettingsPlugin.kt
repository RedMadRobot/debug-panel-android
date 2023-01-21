package com.redmadrobot.debug.plugin.appsettings

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import com.redmadrobot.debug.appsettings.ui.ApplicationSettingsScreen
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.appsettings.data.DefaultSharedPreferences

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

    @Composable
    override fun content() {
        ApplicationSettingsScreen()
    }
}
