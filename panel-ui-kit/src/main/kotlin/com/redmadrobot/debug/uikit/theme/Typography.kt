package com.redmadrobot.debug.uikit.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** Monospace font family for technical values (URLs, config keys, code). */
public val MonoFontFamily: FontFamily = FontFamily.Monospace

@Stable
public data class DebugPanelTypographyTokens(
    /** TopAppBar title. */
    val titleLarge: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    /** Section headers, group names. */
    val titleMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    /** Card titles, server names. */
    val titleSmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp,
    ),
    /** Primary body text. */
    val bodyLarge: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    /** Secondary body text, config values. */
    val bodyMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    /** Technical values (URLs, keys) — monospace. */
    val bodySmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.4.sp,
        fontFamily = MonoFontFamily,
    ),
    /** Tab labels, chip text, button labels. */
    val labelLarge: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    /** Badges, source indicators. */
    val labelMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    /** Section headers (uppercase), overline text. */
    val labelSmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.sp,
    ),
)

internal val DarkDebugPanelTypographyTokens = DebugPanelTypographyTokens(
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.4.sp,
        fontFamily = MonoFontFamily,
        color = BaseColors.Neutral80,
    ),
)

internal fun DebugPanelTypographyTokens.toMaterialTypography(): Typography = Typography(
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall,
)
