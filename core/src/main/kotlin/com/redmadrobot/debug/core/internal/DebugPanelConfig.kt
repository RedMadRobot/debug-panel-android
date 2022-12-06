package com.redmadrobot.debug.core.internal

public class DebugPanelConfig(
    public val shakerMode: Boolean
) {
    internal companion object {
       internal val defaultConfig = DebugPanelConfig(shakerMode = true)
    }
}
