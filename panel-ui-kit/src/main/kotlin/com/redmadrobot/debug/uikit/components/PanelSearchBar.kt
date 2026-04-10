package com.redmadrobot.debug.uikit.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.uikit.R
import com.redmadrobot.debug.uikit.theme.DebugPanelDimensions
import com.redmadrobot.debug.uikit.theme.DebugPanelShapes
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme
import com.redmadrobot.debug.uikit.theme.MonoFontFamily

@Suppress("LongMethod")
@Composable
public fun PanelSearchBar(
    query: String,
    placeholder: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val minHeight by animateDpAsState(
        targetValue = if (query.isNotEmpty()) 48.dp else 40.dp,
        label = "",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .background(
                color = DebugPanelTheme.colors.surface.secondary,
                shape = DebugPanelShapes.medium,
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_search),
            contentDescription = null,
            tint = DebugPanelTheme.colors.content.tertiary,
            modifier = Modifier.size(size = DebugPanelDimensions.iconSizeSmall),
        )
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(weight = 1f)
                .padding(horizontal = 8.dp),
            textStyle = DebugPanelTheme.typography.bodyMedium.copy(
                fontFamily = MonoFontFamily,
                color = DebugPanelTheme.colors.content.primary,
            ),
            singleLine = true,
            cursorBrush = SolidColor(value = DebugPanelTheme.colors.content.accent),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = DebugPanelTheme.typography.bodyMedium,
                        color = DebugPanelTheme.colors.content.tertiary,
                    )
                }
                innerTextField()
            },
        )
        if (query.isNotEmpty()) {
            IconButton(
                onClick = { onQueryChange("") },
                modifier = Modifier.size(size = DebugPanelDimensions.iconSizeLarge),
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_clear),
                    contentDescription = null,
                    tint = DebugPanelTheme.colors.content.tertiary,
                    modifier = Modifier.size(size = DebugPanelDimensions.iconSizeSmall),
                )
            }
        }
    }
}
