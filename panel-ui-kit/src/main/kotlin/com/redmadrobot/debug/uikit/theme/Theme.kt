package com.redmadrobot.debug.uikit.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.redmadrobot.debug.uikit.theme.model.ThemeMode

internal val LocalColors = compositionLocalOf { LightDebugPanelColors }
internal val LocalTypography = compositionLocalOf { DebugPanelTypographyTokens() }

/**
 * Entry point for accessing Debug Panel design tokens.
 *
 * Usage:
 * ```
 * color = DebugPanelTheme.colors.background.primary
 * style = DebugPanelTheme.typography.bodySmall
 * ```
 */
public object DebugPanelTheme {
    public val colors: DebugPanelColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    public val typography: DebugPanelTypographyTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

/**
 * Debug Panel theme.
 *
 * Provides [DebugPanelColors] and [DebugPanelTypographyTokens] via [CompositionLocal],
 * and configures Material 3 [ColorScheme] for standard M3 components.
 *
 * When [themeMode] changes at runtime (e.g. from settings),
 * the entire UI recomposes with animated color transitions.
 *
 * @param themeMode the current [ThemeMode] selection.
 * @param dynamicColor whether to use Material You dynamic colors on supported devices.
 * @param systemBarsColors optional lambda that maps the resolved [DebugPanelColors] to
 *   [SystemBarsColors]. Pass `null` to skip system bar coloring entirely. Defaults to
 *   [SystemBarsColors.fromTheme] which uses [BackgroundColors.primary] for both bars.
 * @param content the composable content to be themed.
 */
@Composable
public fun DebugPanelTheme(
    themeMode: ThemeMode,
    dynamicColor: Boolean = true,
    systemBarsColors: ((DebugPanelColors) -> SystemBarsColors)? = SystemBarsColors::fromTheme,
    content: @Composable () -> Unit,
) {
    val isDarkTheme = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    val targetColors = if (isDarkTheme) DarkDebugPanelColors else LightDebugPanelColors
    val panelColors = targetColors.animated()
    val panelTypography = if (isDarkTheme) DarkDebugPanelTypographyTokens else DebugPanelTypographyTokens()

    val materialColorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        panelColors.toMaterialColorScheme()
    }

    systemBarsColors?.let { colors ->
        SystemBarsEffect(systemBarsColors = colors.invoke(panelColors))
    }

    CompositionLocalProvider(
        LocalColors provides panelColors,
        LocalTypography provides panelTypography,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = panelTypography.toMaterialTypography(),
            content = content,
        )
    }
}
