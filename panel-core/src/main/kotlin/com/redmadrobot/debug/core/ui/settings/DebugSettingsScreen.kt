package com.redmadrobot.debug.core.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun DebugSettingsScreen(
    navController: NavController,
    pluginItems: ImmutableList<PluginSettingsItem>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DebugPanelTheme.colors.background.primary),
    ) {
        items(items = pluginItems, key = { it.pluginName }) { item ->
            PluginItem(
                pluginName = item.pluginName,
                onClick = { navController.navigate(item.pluginClassName) },
            )
        }
    }
}

@Composable
private fun PluginItem(pluginName: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .fillMaxSize()
            .clickable { onClick.invoke() }
            .padding(all = 16.dp),
        text = pluginName,
        style = DebugPanelTheme.typography.titleMedium,
        color = DebugPanelTheme.colors.content.primary,
    )
}
