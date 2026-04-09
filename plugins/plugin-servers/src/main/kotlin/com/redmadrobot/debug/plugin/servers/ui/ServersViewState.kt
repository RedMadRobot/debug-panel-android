package com.redmadrobot.debug.plugin.servers.ui

import androidx.compose.runtime.Immutable
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer

@Immutable
internal data class ServersViewState(
    val preInstalledServers: List<ServerItemData> = emptyList(),
    val addedServers: List<ServerItemData> = emptyList(),
    val serverDialogState: ServerDialogState = ServerDialogState(),
    val isEditMode: Boolean = false
)

@Immutable
internal data class ServerDialogState(
    val show: Boolean = false,
    val serverName: String = "",
    val serverUrl: String = "",
    val editableServer: DebugServer? = null,
    val inputErrors: ServerDialogErrors? = null
)

internal data class ServerDialogErrors(
    val nameError: Int?,
    val urlError: Int?
)

internal data class ServerItemData(val server: DebugServer, val isSelected: Boolean)
