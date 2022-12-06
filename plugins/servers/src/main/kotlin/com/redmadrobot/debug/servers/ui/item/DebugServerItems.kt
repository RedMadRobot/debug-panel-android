package com.redmadrobot.debug.servers.ui.item

import com.redmadrobot.debug.servers.data.model.DebugServer

internal sealed class DebugServerItems {

    data class Header(val header: String) : DebugServerItems()

    data class PreinstalledServer(
        var debugServer: DebugServer,
        var isSelected: Boolean
    ) : DebugServerItems()

    data class AddedServer(
        var debugServer: DebugServer,
        var isSelected: Boolean
    ) : DebugServerItems()
}
