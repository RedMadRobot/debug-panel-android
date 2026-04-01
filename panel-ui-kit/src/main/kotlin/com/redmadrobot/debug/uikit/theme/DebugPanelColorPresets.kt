package com.redmadrobot.debug.uikit.theme

internal val LightDebugPanelColors = DebugPanelColors()

internal val DarkDebugPanelColors = DebugPanelColors(
    background = BackgroundColors(
        primary = BaseColors.Neutral10,
        secondary = BaseColors.NeutralVariant20,
        tertiary = BaseColors.Purple30,
    ),
    button = ButtonColors(
        primary = BaseColors.Purple80,
        onPrimary = BaseColors.Purple20,
        secondary = BaseColors.Purple30,
        onSecondary = BaseColors.Purple80,
        error = BaseColors.Error80,
        onError = BaseColors.Error20,
    ),
    content = ContentColors(
        primary = BaseColors.Neutral87,
        secondary = BaseColors.Neutral80,
        tertiary = BaseColors.Neutral50,
        accent = BaseColors.Purple80,
        error = BaseColors.Error80,
        teal = BaseColors.Teal,
    ),
    stroke = StrokeColors(
        primary = BaseColors.Neutral30,
        secondary = BaseColors.Neutral40,
    ),
    surface = SurfaceColors(
        primary = BaseColors.Neutral10,
        secondary = BaseColors.NeutralVariant20,
        tertiary = BaseColors.Purple30,
        dialog = BaseColors.NeutralVariant20,
        selected = BaseColors.Purple30,
    ),
    source = SourceColors(
        defaultText = BaseColors.Neutral80,
        defaultBackground = BaseColors.Neutral30,
        debugText = BaseColors.GreenDarkText,
        debugBackground = BaseColors.GreenDark,
        remoteText = BaseColors.OrangeDarkText,
        remoteBackground = BaseColors.OrangeDark,
    ),
)
