package com.redmadrobot.debug.uikit.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

private val ColorAnimSpec = spring<Color>(stiffness = 600f)

@Composable
internal fun DebugPanelColors.animated(): DebugPanelColors = DebugPanelColors(
    background = background.animated(),
    button = button.animated(),
    content = content.animated(),
    stroke = stroke.animated(),
    surface = surface.animated(),
    source = source.animated(),
)

@Composable
private fun animateColor(target: Color): Color {
    val animated by animateColorAsState(
        targetValue = target,
        animationSpec = ColorAnimSpec,
        label = "",
    )
    return animated
}

@Composable
private fun BackgroundColors.animated(): BackgroundColors = BackgroundColors(
    primary = animateColor(primary),
    secondary = animateColor(secondary),
    tertiary = animateColor(tertiary),
)

@Composable
private fun ButtonColors.animated(): ButtonColors = ButtonColors(
    primary = animateColor(primary),
    onPrimary = animateColor(onPrimary),
    secondary = animateColor(secondary),
    onSecondary = animateColor(onSecondary),
    error = animateColor(error),
    onError = animateColor(onError),
)

@Composable
private fun ContentColors.animated(): ContentColors = ContentColors(
    primary = animateColor(primary),
    secondary = animateColor(secondary),
    tertiary = animateColor(tertiary),
    accent = animateColor(accent),
    error = animateColor(error),
    teal = animateColor(teal),
)

@Composable
private fun StrokeColors.animated(): StrokeColors = StrokeColors(
    primary = animateColor(primary),
    secondary = animateColor(secondary),
)

@Composable
private fun SurfaceColors.animated(): SurfaceColors = SurfaceColors(
    primary = animateColor(primary),
    secondary = animateColor(secondary),
    tertiary = animateColor(tertiary),
    dialog = animateColor(dialog),
    selected = animateColor(selected),
)

@Composable
private fun SourceColors.animated(): SourceColors = SourceColors(
    defaultText = animateColor(defaultText),
    defaultBackground = animateColor(defaultBackground),
    debugText = animateColor(debugText),
    debugBackground = animateColor(debugBackground),
    remoteText = animateColor(remoteText),
    remoteBackground = animateColor(remoteBackground),
)
