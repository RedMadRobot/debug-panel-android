package com.redmadrobot.debug.uikit.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

/**
 * Theme mode selectable at runtime from debug panel settings.
 */
public enum class ThemeMode {
    /** Follow system dark/light setting. */
    System,

    /** Always light. */
    Light,

    /** Always dark. */
    Dark,
}

/**
 * Holds the currently selected [ThemeMode].
 *
 * Mutate [themeMode] from settings UI — the composable tree will recompose automatically.
 */
@Stable
public class ThemeState(initialMode: ThemeMode = ThemeMode.System) {
    public var themeMode: ThemeMode by mutableStateOf(initialMode)
}

internal val LocalColors = compositionLocalOf { LightDebugPanelColors }
internal val LocalTypography = compositionLocalOf { DebugPanelTypographyTokens() }
internal val LocalThemeState = compositionLocalOf { ThemeState() }

/**
 * Entry point for accessing Debug Panel design tokens.
 *
 * Usage:
 * ```
 * color = DebugPanelTheme.colors.background.primary
 * style = DebugPanelTheme.typography.bodySmall
 * mode  = DebugPanelTheme.themeState.themeMode
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

    public val themeState: ThemeState
        @Composable
        @ReadOnlyComposable
        get() = LocalThemeState.current
}

/**
 * Debug Panel theme.
 *
 * Provides [DebugPanelColors] and [DebugPanelTypographyTokens] via [CompositionLocal],
 * and configures Material 3 [ColorScheme] for standard M3 components.
 *
 * When [themeState]`.themeMode` changes at runtime (e.g. from settings),
 * the entire UI recomposes with animated color transitions.
 *
 * @param themeState holds the runtime-mutable [ThemeMode]. Shared across the panel so that
 *   settings can write to it and all screens react.
 * @param dynamicColor whether to use Material You dynamic colors on supported devices.
 * @param content the composable content to be themed.
 */
@Composable
public fun DebugPanelTheme(
    themeState: ThemeState = ThemeState(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (themeState.themeMode) {
        ThemeMode.System -> systemDark
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    val targetColors = if (darkTheme) DarkDebugPanelColors else LightDebugPanelColors
    val panelColors = targetColors.animated()
    val panelTypography = if (darkTheme) DarkDebugPanelTypographyTokens else DebugPanelTypographyTokens()

    val materialColorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        panelColors.toMaterialColorScheme()
    }

    CompositionLocalProvider(
        LocalColors provides panelColors,
        LocalTypography provides panelTypography,
        LocalThemeState provides themeState,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = panelTypography.toMaterialTypography(),
            content = content,
        )
    }
}

internal fun DebugPanelColors.toMaterialColorScheme(): ColorScheme = lightColorScheme(
    primary = button.primary,
    onPrimary = button.onPrimary,
    primaryContainer = surface.tertiary,
    onPrimaryContainer = content.primary,
    secondary = content.secondary,
    onSecondary = button.onPrimary,
    secondaryContainer = button.secondary,
    onSecondaryContainer = content.primary,
    tertiary = content.tertiary,
    onTertiary = button.onPrimary,
    error = content.error,
    onError = button.onError,
    errorContainer = BaseColors.Error90,
    onErrorContainer = BaseColors.Error20,
    background = background.primary,
    onBackground = content.primary,
    surface = surface.primary,
    onSurface = content.primary,
    surfaceVariant = stroke.primary,
    onSurfaceVariant = content.secondary,
    outline = content.tertiary,
    outlineVariant = stroke.secondary,
    surfaceContainer = surface.secondary,
    surfaceContainerHigh = surface.tertiary,
    inverseSurface = content.primary,
    inverseOnSurface = background.primary,
    inversePrimary = content.accent,
)
