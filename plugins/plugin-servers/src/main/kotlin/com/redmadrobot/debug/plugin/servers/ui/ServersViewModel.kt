package com.redmadrobot.debug.plugin.servers.ui

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.safeLaunch
import com.redmadrobot.debug.core.internal.PluginViewModel
import com.redmadrobot.debug.plugin.servers.R
import com.redmadrobot.debug.plugin.servers.ServerSelectedEvent
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug.plugin.servers.data.DebugServerRepository
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ServersViewModel(
    private val serversRepository: DebugServerRepository,
) : PluginViewModel() {

    private val _state = MutableStateFlow(ServersViewState())
    val state: StateFlow<ServersViewState> = _state.asStateFlow()

    fun loadServers() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { serversState ->
                serversState.copy(
                    preInstalledServers = serversRepository.getPreInstalledServers()
                        .mapToServerItems(),
                    addedServers = serversRepository.getServers().mapToServerItems(),
                )
            }
        }
    }

    fun onAddClicked() {
        _state.update { serversState ->
            serversState.copy(serverDialogState = serversState.serverDialogState.copy(show = true))
        }
    }

    fun dismissDialogs() {
        _state.update { serversState ->
            serversState.copy(serverDialogState = ServerDialogState())
        }
    }

    fun onServerNameChanged(name: String) {
        _state.update { serversState ->
            serversState.copy(serverDialogState = serversState.serverDialogState.copy(serverName = name))
        }
    }

    fun onServerUrlChanged(url: String) {
        _state.update { serversState ->
            serversState.copy(serverDialogState = serversState.serverDialogState.copy(serverUrl = url))
        }
    }

    fun onSaveServerClicked() {
        val dialogState = _state.value.serverDialogState
        val inputErrors = checkInputErrors(dialogState)

        if (inputErrors != null) {
            _state.update { serversState ->
                serversState.copy(
                    serverDialogState = dialogState.copy(
                        inputErrors = inputErrors
                    )
                )
            }

            return
        }

        if (dialogState.editableServerId == null) {
            addServer(dialogState.serverName, dialogState.serverUrl)
        } else {
            updateServerData(
                dialogState.editableServerId,
                dialogState.serverName,
                dialogState.serverUrl
            )
        }

        _state.update { serversState ->
            serversState.copy(serverDialogState = ServerDialogState())
        }
    }

    private fun checkInputErrors(dialogState: ServerDialogState): ServerDialogErrors? {
        val nameError = R.string.error_empty_name.takeIf { dialogState.serverName.isEmpty() }
        val urlError = R.string.error_wrong_host.takeIf {
            !Patterns.WEB_URL.matcher(dialogState.serverUrl).matches()
        }

        return if (nameError != null || urlError != null) {
            ServerDialogErrors(nameError = nameError, urlError = urlError)
        } else {
            null
        }
    }

    fun onRemoveServerClicked(debugServer: DebugServer) {
        viewModelScope.safeLaunch {
            serversRepository.removeServer(debugServer)
            _state.update { serversState ->
                serversState.copy(
                    addedServers = serversRepository.getServers().mapToServerItems()
                )
            }
        }
    }

    private fun addServer(name: String, url: String) {
        val server = DebugServer(name = name, url = url, isDefault = false)
        viewModelScope.safeLaunch {
            serversRepository.addServer(server)
            _state.update { serversState ->
                serversState.copy(
                    addedServers = serversRepository.getServers().mapToServerItems()
                )
            }
        }
    }

    private fun updateServerData(id: Int, name: String, url: String) {
        val serverForUpdate = _state.value.addedServers
            .find { it.server.id == id }
            ?.server

        serverForUpdate?.let {
            val updatedServer = serverForUpdate.copy(name = name, url = url)
            viewModelScope.safeLaunch {
                serversRepository.updateServer(updatedServer)
                _state.update { serversState ->
                    serversState.copy(
                        addedServers = serversRepository.getServers().mapToServerItems()
                    )
                }
            }
        }
    }

    fun onEditServerClicked(debugServer: DebugServer) {
        _state.update { serversState ->
            serversState.copy(
                serverDialogState = ServerDialogState(
                    editableServerId = debugServer.id,
                    serverName = debugServer.name,
                    serverUrl = debugServer.url,
                    show = true
                )
            )
        }
    }

    fun onServerClicked(debugServer: DebugServer) {
        viewModelScope.launch {
            val selectedServer = serversRepository.getSelectedServer()
            if (debugServer != selectedServer) {
                getPlugin<ServersPlugin>().pushEvent(ServerSelectedEvent(debugServer))
                serversRepository.saveSelectedServer(debugServer)
                loadServers()
            }
        }
    }

    private suspend fun List<DebugServer>.mapToServerItems(): List<ServerItemData> {
        val selectedServer = serversRepository.getSelectedServer()
        return map { debugServer ->
            val isSelected = debugServer == selectedServer
            ServerItemData(debugServer, isSelected)
        }
    }
}
