package com.redmadrobot.debug.uikit.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.uikit.theme.DebugPanelDimensions
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme

@Composable
public fun PanelToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (trackColor, thumbOffset) = with(DebugPanelTheme.colors) {
        if (checked) content.teal to 22.dp else stroke.primary to 2.dp
    }
    val animatedTrackColor by animateColorAsState(targetValue = trackColor, label = "")
    val animatedThumbOffset by animateDpAsState(targetValue = thumbOffset, label = "")

    Box(
        modifier = modifier
            .width(width = DebugPanelDimensions.toggleWidth)
            .height(height = DebugPanelDimensions.toggleHeight)
            .clip(shape = RoundedCornerShape(size = 12.dp))
            .background(color = animatedTrackColor)
            .clickable { onCheckedChange(!checked) },
    ) {
        Box(
            modifier = Modifier
                .padding(start = animatedThumbOffset, top = 2.dp)
                .size(size = 20.dp)
                .background(color = Color.White, shape = CircleShape),
        )
    }
}
