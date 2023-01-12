package com.redmadrobot.debug.plugin.servers.ui.item

import com.redmadrobot.debug.plugin.servers.data.model.DebugServerData

internal sealed class DebugServerItems {

    data class Header(val header: String) : DebugServerItems()

    data class PreinstalledServer(
        var debugServer: DebugServerData,
        var isSelected: Boolean
    ) : DebugServerItems()

    data class AddedServer(
        var debugServer: DebugServerData,
        var isSelected: Boolean
    ) : DebugServerItems()
}
