package com.redmadrobot.debug.core.ui.settings

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.internal.EditablePlugin

private const val MAIN_SCREEN_ROUTE = "main"

@Composable
internal fun DebugSettingsNavHost(
    navController: NavHostController,
    pluginItems: List<PluginSettingsItem>,
) {
    NavHost(navController = navController, startDestination = MAIN_SCREEN_ROUTE) {
        composable(MAIN_SCREEN_ROUTE) {
            DebugSettingsScreen(pluginItems = pluginItems, navController = navController)
        }

        pluginItems.forEach { plugin ->
            composable(plugin.pluginClassName) {
                (getPlugin(plugin.pluginClassName) as? EditablePlugin)?.settingsContent()
            }
        }
    }
}