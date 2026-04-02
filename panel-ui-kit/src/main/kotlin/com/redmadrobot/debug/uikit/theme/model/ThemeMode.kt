package com.redmadrobot.debug.uikit.theme.model

import com.redmadrobot.debug.uikit.R

/**
 * Theme mode selectable at runtime from debug panel settings.
 */
public enum class ThemeMode {
    /** Follow system dark/light setting. */
    System,

    /** Always light. */
    Light,

    /** Always dark. */
    Dark;

    public companion object {
        public fun getTitleRes(mode: ThemeMode): Int {
            return when (mode) {
                System -> R.string.debug_panel_theme_system
                Light -> R.string.debug_panel_theme_light
                Dark -> R.string.debug_panel_theme_dark
            }
        }

        public fun getIconRes(mode: ThemeMode): Int {
            return when (mode) {
                System -> R.drawable.icon_brightness_auto
                Light -> R.drawable.icon_light_mode
                Dark -> R.drawable.icon_dark_mode
            }
        }
    }
}
