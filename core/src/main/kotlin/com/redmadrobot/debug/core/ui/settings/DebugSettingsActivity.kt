package com.redmadrobot.debug.core.ui.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.redmadrobot.debug.core.extension.getAllPlugins
import com.redmadrobot.debug.core.internal.EditablePlugin


internal class DebugSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val pluginItems = remember { getSettingItems() }
                DebugSettingsNavHost(navController = navController, pluginItems = pluginItems)
            }
        }
    }

    private fun getSettingItems(): List<PluginSettingsItem> {
        return getAllPlugins()
            .filter { it is EditablePlugin }
            .map { plugin ->
                PluginSettingsItem(
                    pluginClassName = plugin::class.java.name,
                    pluginName = plugin.getName(),
                )
            }
    }
}