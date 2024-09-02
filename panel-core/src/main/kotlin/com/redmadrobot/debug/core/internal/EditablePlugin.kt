package com.redmadrobot.debug.core.internal

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.annotation.DebugPanelInternal

/**
 * Plugin that will be displayed when opening the settings
 */
@DebugPanelInternal
public interface EditablePlugin {
    @Composable
    public fun settingsContent() {
    }
}