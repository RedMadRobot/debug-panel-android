package com.redmadrobot.debug.appsettings.plugin

import android.content.SharedPreferences
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import com.redmadrobot.app_settings_plugin.databinding.FragmentContainerAppSettingsBinding
import com.redmadrobot.debug.appsettings.data.DefaultSharedPreferences
import com.redmadrobot.debug.appsettings.ui.ApplicationSettingsFragment
import com.redmadrobot.debug.core.CommonContainer
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.plugin.PluginDependencyContainer

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

    @Deprecated(
        "You should't use fragments for you plugins. Please use Jetpack Compose",
        replaceWith = ReplaceWith("content()", "com.redmadrobot.debug.core.plugin.Plugin")
    )
    override fun getFragment(): Fragment {
        return ApplicationSettingsFragment()
    }

    @Composable
    override fun content() {
        AndroidViewBinding(
            FragmentContainerAppSettingsBinding::inflate,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}
