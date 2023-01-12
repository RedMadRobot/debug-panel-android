package com.redmadrobot.debug.servers.ui

import com.redmadrobot.debug.servers.ui.item.DebugServerItems

internal data class ServersViewState(
    val preInstalledServers: List<DebugServerItems> = emptyList(),
    val addedServers: List<DebugServerItems> = emptyList(),
    val preinstalledStages: List<DebugServerItems> = emptyList(),
    val addedStages: List<DebugServerItems> = emptyList(),
)
