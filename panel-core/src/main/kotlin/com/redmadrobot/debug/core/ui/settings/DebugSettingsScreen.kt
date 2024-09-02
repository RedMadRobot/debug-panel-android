package com.redmadrobot.debug.core.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
internal fun DebugSettingsScreen(
    navController: NavController,
    pluginItems: List<PluginSettingsItem>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(pluginItems) { item ->
            PluginItem(
                pluginName = item.pluginName,
                onClick = { navController.navigate(item.pluginClassName) },
            )
        }
    }
}

@Composable
private fun PluginItem(pluginName: String, onClick: () -> Unit) {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick.invoke() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        text = pluginName,
        fontSize = 18.sp,
    )
}