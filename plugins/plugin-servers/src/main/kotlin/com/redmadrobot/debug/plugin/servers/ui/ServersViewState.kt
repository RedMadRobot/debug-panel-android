package com.redmadrobot.debug.plugin.servers.ui

import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.model.DebugStage

internal data class ServersViewState(
    val preInstalledServers: List<ServerItemData> = emptyList(),
    val addedServers: List<ServerItemData> = emptyList(),
    val preInstalledStages: List<StageItemData> = emptyList(),
    val addedStages: List<StageItemData> = emptyList(),
    val serverDialogState: ServerDialogState = ServerDialogState()
)

internal data class ServerDialogState(
    val show: Boolean = false,
    val serverName: String = "",
    val serverUrl: String = "",
    val editableServerId: Int? = null,
    val inputErrors: ServerDialogErrors? = null
)

internal data class ServerDialogErrors(
    val nameError: Int?,
    val urlError: Int?
)

internal data class ServerItemData(val server: DebugServer, val isSelected: Boolean)
internal data class StageItemData(val server: DebugStage, val isSelected: Boolean)
