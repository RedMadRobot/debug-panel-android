package com.redmadrobot.debug.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme
import com.redmadrobot.debug.uikit.theme.SystemBarsColors
import com.redmadrobot.debug.uikit.theme.SystemBarsEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun PanelBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = DebugPanelTheme.colors.surface.secondary,
        modifier = modifier,
        dragHandle = { SheetHandle() },
    ) {
        SystemBarsEffect(
            systemBarsColors = SystemBarsColors(
                statusBarColor = DebugPanelTheme.colors.background.primary,
                navigationBarColor = DebugPanelTheme.colors.surface.secondary,
            ),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            Text(
                text = title,
                style = DebugPanelTheme.typography.titleLarge,
                color = DebugPanelTheme.colors.content.primary,
                modifier = Modifier.padding(bottom = 20.dp),
            )
            content()
        }
    }
}

@Composable
private fun SheetHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(top = 12.dp, bottom = 20.dp)
            .size(width = 32.dp, height = 4.dp)
            .background(
                color = DebugPanelTheme.colors.stroke.secondary,
                shape = RoundedCornerShape(2.dp),
            ),
    )
}
