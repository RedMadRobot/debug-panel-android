package com.redmadrobot.debug.servers.ui

import com.redmadrobot.debug.servers.ui.item.DebugServerItems

internal data class ServersViewState(
    val preInstalledServers: List<DebugServerItems>,
    val addedServers: List<DebugServerItems>
)
