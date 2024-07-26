package com.redmadrobot.debug.plugin.servers.ui

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.core.internal.PluginViewModel
import com.redmadrobot.debug.core.extension.safeLaunch
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.plugin.servers.R
import com.redmadrobot.debug.plugin.servers.ServerSelectedEvent
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug.plugin.servers.StageSelectedEvent
import com.redmadrobot.debug.plugin.servers.data.DebugServerRepository
import com.redmadrobot.debug.plugin.servers.data.DebugStageRepository
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.model.DebugStage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ServersViewModel(
    private val serversRepository: DebugServerRepository,
    private val stagesRepository: DebugStageRepository
) : PluginViewModel() {

    private val _state = MutableStateFlow(ServersViewState())
    val state: StateFlow<ServersViewState> = _state.asStateFlow()

    fun loadServers() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    preInstalledServers = serversRepository.getPreInstalledServers()
                        .mapToServerItems(),
                    preInstalledStages = stagesRepository.getPreInstalledStages().mapToStageItems(),
                    addedServers = serversRepository.getServers().mapToServerItems(),
                    addedStages = stagesRepository.getStages().mapToStageItems()
                )
            }
        }
    }

    fun onAddClicked() {
        _state.update {
            it.copy(serverDialogState = it.serverDialogState.copy(show = true))
        }
    }

    fun dismissDialogs() {
        _state.update {
            it.copy(serverDialogState = ServerDialogState())
        }
    }

    fun onServerNameChanged(name: String) {
        _state.update {
            it.copy(serverDialogState = it.serverDialogState.copy(serverName = name))
        }
    }

    fun onServerUrlChanged(url: String) {
        _state.update {
            it.copy(serverDialogState = it.serverDialogState.copy(serverUrl = url))
        }
    }

    fun onSaveServerClicked() {
        if (allServerFieldValid()) {
            val dialogState = _state.value.serverDialogState

            if (dialogState.editableServerId == null) {
                addServer(dialogState.serverName, dialogState.serverUrl)
            } else {
                updateServerData(
                    dialogState.editableServerId,
                    dialogState.serverName,
                    dialogState.serverUrl
                )
            }

            _state.update {
                it.copy(serverDialogState = ServerDialogState())
            }
        }
    }

    private fun allServerFieldValid(): Boolean {
        val dialogState = _state.value.serverDialogState
        val nameError = if (dialogState.serverName.isEmpty()) {
            R.string.error_empty_name
        } else {
            null
        }
        val urlError = if (!Patterns.WEB_URL.matcher(dialogState.serverUrl).matches()) {
            R.string.error_wrong_host
        } else {
            null
        }

        _state.update {
            it.copy(
                serverDialogState = dialogState.copy(
                    nameError = nameError,
                    urlError = urlError
                )
            )
        }

        return nameError == null && urlError == null
    }

    fun onRemoveServerClicked(debugServer: DebugServer) {
        viewModelScope.safeLaunch {
            serversRepository.removeServer(debugServer)
            _state.update {
                it.copy(
                    addedServers = serversRepository.getServers().mapToServerItems()
                )
            }
        }
    }

    private fun addServer(name: String, url: String) {
        val server = DebugServer(name = name, url = url, isDefault = false)
        viewModelScope.safeLaunch {
            serversRepository.addServer(server)
            _state.update {
                it.copy(
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
                _state.update {
                    it.copy(addedServers = serversRepository.getServers().mapToServerItems())
                }
            }
        }
    }

    fun onEditServerClicked(debugServer: DebugServer) {
        _state.update {
            it.copy(
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

    fun onStageClicked(debugStage: DebugStage) {
        viewModelScope.launch {
            val selectedStage = stagesRepository.getSelectedStage()
            if (debugStage != selectedStage) {
                stagesRepository.saveSelectedStage(debugStage)
                getPlugin<ServersPlugin>().pushEvent(StageSelectedEvent(debugStage))
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

    private fun List<DebugStage>.mapToStageItems(): List<StageItemData> {
        val selectedStage = stagesRepository.getSelectedStage()
        return map { debugStage ->
            val isSelected = debugStage == selectedStage
            StageItemData(debugStage, isSelected)
        }
    }
}
