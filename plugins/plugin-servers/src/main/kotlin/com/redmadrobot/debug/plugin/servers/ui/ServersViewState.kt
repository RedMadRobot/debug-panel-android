package com.redmadrobot.debug.plugin.servers.ui

import com.redmadrobot.debug.plugin.servers.data.model.DebugServer

internal data class ServersViewState(
    val preInstalledServers: List<ServerItemData> = emptyList(),
    val addedServers: List<ServerItemData> = emptyList(),
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
