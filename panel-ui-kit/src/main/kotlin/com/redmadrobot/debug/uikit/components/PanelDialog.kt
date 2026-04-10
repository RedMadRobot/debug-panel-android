package com.redmadrobot.debug.uikit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.redmadrobot.debug.uikit.theme.DebugPanelShapes
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme

@Composable
public fun PanelDialog(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = DebugPanelShapes.dialog,
            color = DebugPanelTheme.colors.surface.dialog,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Column(modifier = Modifier.padding(all = 24.dp)) {
                Text(
                    text = title,
                    style = DebugPanelTheme.typography.titleLarge,
                    color = DebugPanelTheme.colors.content.primary,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                content()
            }
        }
    }
}
