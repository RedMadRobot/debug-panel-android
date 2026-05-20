package com.redmadrobot.debug.core.internal

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.annotation.DebugPanelInternal

/**
 * Interface for plugins providing a settings screen.
 *
 * A plugin implementing this interface will be displayed
 * in the settings section of the debug panel.
 *
 * @see com.redmadrobot.debug.plugin.servers.ServersPlugin -- example implementation
 */
@DebugPanelInternal
public interface EditablePlugin {
    /**
     * Composable function rendering the plugin's settings UI.
     */
    @Suppress("TopLevelComposableFunctions", "ComposableFunctionName")
    @Composable
    public fun settingsContent() {
    }
}
