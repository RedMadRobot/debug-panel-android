package com.redmadrobot.debug.uikit.theme

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat

/**
 * Applies [systemBarsColors] to the host window on every recomposition.
 *
 * Works both inside an Activity (`setContent { }`) and inside popup windows
 * such as `ModalBottomSheet` (detected via [DialogWindowProvider]).
 *
 * - Uses [WindowInsetsControllerCompat][androidx.core.view.WindowInsetsControllerCompat]
 *   for icon tinting across all supported API levels.
 * - Sets `window.statusBarColor` / `window.navigationBarColor` for bar backgrounds
 *   (effective on API 23-34; no-op on API 35+ where edge-to-edge is enforced).
 * - Light/dark icon style is chosen automatically via color luminance:
 *   bright background → dark icons (`isAppearanceLightXxxBars = true`).
 *
 * If the current [LocalView] is not attached to an Activity or dialog window,
 * the effect is silently skipped.
 */
@Composable
public fun SystemBarsEffect(systemBarsColors: SystemBarsColors) {
    val view = LocalView.current
    if (view.isInEditMode) return

    val statusArgb = systemBarsColors.statusBarColor.toArgb()
    val navArgb = systemBarsColors.navigationBarColor.toArgb()
    val lightStatus = systemBarsColors.statusBarColor.isLight()
    val lightNav = systemBarsColors.navigationBarColor.isLight()

    SideEffect {
        val window = findWindow(view) ?: return@SideEffect

        @Suppress("DEPRECATION")
        window.statusBarColor = statusArgb
        @Suppress("DEPRECATION")
        window.navigationBarColor = navArgb

        val controller = WindowCompat.getInsetsController(window, view)
        controller.isAppearanceLightStatusBars = lightStatus
        controller.isAppearanceLightNavigationBars = lightNav
    }
}

/** BT.709 coefficient for red channel. */
private const val LUMINANCE_RED = 0.2126f

/** BT.709 coefficient for green channel. */
private const val LUMINANCE_GREEN = 0.7152f

/** BT.709 coefficient for blue channel. */
private const val LUMINANCE_BLUE = 0.0722f

/** Luminance threshold above which the color is considered "light". */
private const val LUMINANCE_THRESHOLD = 0.5f

/**
 * Returns `true` when the color's relative luminance is high enough
 * that dark (black) icons should be drawn on top of it.
 *
 * Uses the BT.709 coefficients for relative luminance. Works on all API levels
 * (no dependency on [android.graphics.Color.luminance] which requires API 26).
 */
private fun Color.isLight(): Boolean {
    val luminance = LUMINANCE_RED * red + LUMINANCE_GREEN * green + LUMINANCE_BLUE * blue
    return luminance > LUMINANCE_THRESHOLD
}

/**
 * Resolves the [Window] for the current view.
 *
 * Checks [DialogWindowProvider] first (covers `ModalBottomSheet` and other popup windows),
 * then falls back to the host Activity's window.
 */
private fun findWindow(view: View): Window? {
    return (view.parent as? DialogWindowProvider)?.window
        ?: (view.context as? Activity)?.window
}
