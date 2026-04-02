package com.redmadrobot.debug.uikit.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

/**
 * Colors applied to the system status bar and navigation bar.
 *
 * By default, both bars use the primary background color of the current theme.
 * Light/dark icon appearance is determined automatically based on color luminance.
 *
 * Consumers can override the mapping by providing a custom [SystemBarsColors] instance
 * or a `systemBarsColors` lambda to [DebugPanelTheme].
 */
@Stable
public data class SystemBarsColors(
    /** Color of the system status bar. */
    val statusBarColor: Color,
    /** Color of the system navigation bar. */
    val navigationBarColor: Color,
) {
    public companion object {
        /**
         * Default mapping: both bars use [DebugPanelColors.background] primary.
         */
        public fun fromTheme(colors: DebugPanelColors): SystemBarsColors = SystemBarsColors(
            statusBarColor = colors.background.primary,
            navigationBarColor = colors.background.primary,
        )
    }
}
