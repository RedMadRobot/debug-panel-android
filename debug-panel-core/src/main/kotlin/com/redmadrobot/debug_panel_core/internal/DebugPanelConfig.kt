package com.redmadrobot.debug_panel_core.internal

public class DebugPanelConfig(
    public val shakerMode: Boolean
) {
    internal companion object {
        val defaultConfig = DebugPanelConfig(shakerMode = true)
    }
}