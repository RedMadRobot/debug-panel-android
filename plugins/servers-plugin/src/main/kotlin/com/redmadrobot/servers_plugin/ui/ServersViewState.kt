package com.redmadrobot.servers_plugin.ui

import com.redmadrobot.servers_plugin.ui.item.DebugServerItems

internal data class ServersViewState(
    val preInstalledServers: List<DebugServerItems>,
    val addedServers: List<DebugServerItems>
)
