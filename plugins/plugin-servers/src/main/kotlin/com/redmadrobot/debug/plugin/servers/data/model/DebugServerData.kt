package com.redmadrobot.debug.plugin.servers.data.model

import com.redmadrobot.debug.core.annotation.DebugPanelInternal

@DebugPanelInternal
public interface DebugServerData {
    public val id: Int
    public val name: String
    public val isDefault: Boolean
}
