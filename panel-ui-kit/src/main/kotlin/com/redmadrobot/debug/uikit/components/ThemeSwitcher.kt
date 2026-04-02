package com.redmadrobot.debug.uikit.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme
import com.redmadrobot.debug.uikit.theme.model.ThemeMode

@Composable
public fun ThemeSwitcher(
    currentMode: ThemeMode,
    onModeSelect: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }, modifier = modifier) {
        Icon(
            painter = painterResource(id = ThemeMode.getIconRes(mode = currentMode)),
            contentDescription = null,
            tint = DebugPanelTheme.colors.content.secondary,
            modifier = Modifier.size(size = 20.dp),
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ThemeMode.entries.forEach { mode ->
                key(mode.name) {
                    DropdownMenuItem(
                        title = stringResource(id = ThemeMode.getTitleRes(mode = mode)),
                        onModeSelect = {
                            onModeSelect.invoke(mode)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DropdownMenuItem(
    title: String,
    onModeSelect: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text = title, style = DebugPanelTheme.typography.bodyMedium) },
        onClick = { onModeSelect() },
    )
}
