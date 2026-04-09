package com.redmadrobot.debug.plugin.servers.ui

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.safeLaunch
import com.redmadrobot.debug.core.internal.PluginViewModel
import com.redmadrobot.debug.plugin.servers.R
import com.redmadrobot.debug.plugin.servers.ServerSelectedEvent
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug.plugin.servers.data.DebugServerRepository
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ServersViewModel(
    private val isEditMode: Boolean,
    private val serversRepository: DebugServerRepository,
) : PluginViewModel() {
    private val _state = MutableStateFlow(ServersViewState(isEditMode = isEditMode))
    val state: StateFlow<ServersViewState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<ServerSelectionMessageEvent?>()
    val events = _events.asSharedFlow().distinctUntilChanged()

    init {
        loadServers()
    }

    fun onServerClicked(debugServer: DebugServer) {
        if (!isEditMode) {
            selectServer(debugServer)
            viewModelScope.launch { _events.emit(ServerSelectionMessageEvent(serverName = debugServer.name)) }
        } else {
            editServer(debugServer)
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

        if (dialogState.editableServer == null) {
            addServer(dialogState.serverName, dialogState.serverUrl)
        } else {
            updateServerData(
                oldServer = dialogState.editableServer,
                name = dialogState.serverName,
                url = dialogState.serverUrl
            )
        }

        _state.update { serversState ->
            serversState.copy(serverDialogState = ServerDialogState())
        }
    }

    private fun loadServers() {
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
            loadServers()
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

    private fun updateServerData(oldServer: DebugServer, name: String, url: String) {
        val updatedServer = oldServer.copy(name = name, url = url)

        viewModelScope.safeLaunch {
            serversRepository.updateServer(oldServer = oldServer, newServer = updatedServer)
            _state.update { serversState ->
                serversState.copy(
                    addedServers = serversRepository.getServers().mapToServerItems()
                )
            }
        }
    }

    private fun editServer(debugServer: DebugServer) {
        _state.update { serversState ->
            serversState.copy(
                serverDialogState = ServerDialogState(
                    editableServer = debugServer,
                    serverName = debugServer.name,
                    serverUrl = debugServer.url,
                    show = true
                )
            )
        }
    }

    private fun selectServer(debugServer: DebugServer) {
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

internal data class ServerSelectionMessageEvent(val serverName: String) : DebugEvent
