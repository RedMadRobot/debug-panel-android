package com.redmadrobot.debug.uikit.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
public data class BackgroundColors(
    val primary: Color = BaseColors.Purple99,
    val secondary: Color = BaseColors.Purple95,
    val tertiary: Color = BaseColors.Purple90,
)

@Stable
public data class ButtonColors(
    val primary: Color = BaseColors.Purple40,
    val onPrimary: Color = BaseColors.White,
    val secondary: Color = BaseColors.Purple90,
    val onSecondary: Color = BaseColors.Purple40,
    val error: Color = BaseColors.Error40,
    val onError: Color = BaseColors.White,
)

@Stable
public data class ContentColors(
    val primary: Color = BaseColors.Neutral10,
    val secondary: Color = BaseColors.Neutral30,
    val tertiary: Color = BaseColors.Neutral50,
    val accent: Color = BaseColors.Purple40,
    val error: Color = BaseColors.Error40,
    val teal: Color = BaseColors.Teal,
)

@Stable
public data class StrokeColors(
    val primary: Color = BaseColors.Neutral95,
    val secondary: Color = BaseColors.Neutral80,
)

@Stable
public data class SurfaceColors(
    val primary: Color = BaseColors.Purple99,
    val secondary: Color = BaseColors.Purple95,
    val tertiary: Color = BaseColors.Purple90,
    val dialog: Color = BaseColors.Neutral99,
    val selected: Color = BaseColors.Purple90,
)

@Stable
public data class SourceColors(
    val defaultText: Color = BaseColors.Neutral50,
    val defaultBackground: Color = BaseColors.Neutral95,
    val debugText: Color = BaseColors.Green,
    val debugBackground: Color = BaseColors.GreenLight,
    val remoteText: Color = BaseColors.Orange,
    val remoteBackground: Color = BaseColors.OrangeLight,
)

@Stable
public data class DebugPanelColors(
    val background: BackgroundColors = BackgroundColors(),
    val button: ButtonColors = ButtonColors(),
    val content: ContentColors = ContentColors(),
    val stroke: StrokeColors = StrokeColors(),
    val surface: SurfaceColors = SurfaceColors(),
    val source: SourceColors = SourceColors(),
)
