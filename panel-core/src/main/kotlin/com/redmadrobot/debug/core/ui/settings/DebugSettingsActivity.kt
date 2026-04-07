package com.redmadrobot.debug.core.ui.settings

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.core.extension.getAllPlugins
import com.redmadrobot.debug.core.internal.EditablePlugin
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme
import kotlinx.collections.immutable.toImmutableList

internal class DebugSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val debugPanel = remember { DebugPanel.getInstance()!! }
            val themeMode by debugPanel.observeDebugPanelTheme()
                .collectAsStateWithLifecycle(initialValue = debugPanel.getSelectedTheme())

            DebugPanelTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                val pluginItems = remember { getSettingItems() }
                DebugSettingsNavHost(
                    navController = navController,
                    pluginItems = pluginItems.toImmutableList(),
                )
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
