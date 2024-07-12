package com.redmadrobot.debug.plugin.servers.ui

import com.redmadrobot.debug.plugin.servers.ui.item.DebugServerItems

internal data class ServersViewState(
    val preInstalledServers: List<DebugServerItems>,
    val addedServers: List<DebugServerItems>
)
