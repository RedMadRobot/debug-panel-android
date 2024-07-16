package com.redmadrobot.debug.servers.ui

import com.redmadrobot.debug.servers.data.model.DebugServer
import com.redmadrobot.debug.servers.data.model.DebugStage

internal data class ServersViewState(
    val preInstalledServers: List<ServerItemData> = emptyList(),
    val addedServers: List<ServerItemData> = emptyList(),
    val preinstalledStages: List<StageItemData> = emptyList(),
    val addedStages: List<StageItemData> = emptyList(),
    val serverDialogState: ServerDialogState = ServerDialogState()
)

internal data class ServerDialogState(
    val show: Boolean = false,
    val serverName: String = "",
    val serverUrl: String = "",
    val editableServerId: Int? = null,
    val nameError: Int? = null,
    val urlError: Int? = null,
)

internal data class ServerItemData(val server: DebugServer, val isSelected: Boolean)
internal data class StageItemData(val server: DebugStage, val isSelected: Boolean)
