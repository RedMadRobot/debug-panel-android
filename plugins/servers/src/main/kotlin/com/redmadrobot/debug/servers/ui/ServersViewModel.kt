package com.redmadrobot.debug.servers.ui

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.common.base.PluginViewModel
import com.redmadrobot.debug.common.extension.safeLaunch
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.servers.data.DebugServerRepository
import com.redmadrobot.debug.servers.data.DebugStageRepository
import com.redmadrobot.debug.servers.data.model.DebugServer
import com.redmadrobot.debug.servers.data.model.DebugStage
import com.redmadrobot.debug.servers.plugin.ServerSelectedEvent
import com.redmadrobot.debug.servers.plugin.ServersPlugin
import com.redmadrobot.debug.servers.plugin.StageSelectedEvent
import com.redmadrobot.debug.servers.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ServersViewModel(
    private val serversRepository: DebugServerRepository,
    private val stagesRepository: DebugStageRepository
) : PluginViewModel() {

    private val state_ = MutableStateFlow(ServersViewState())
    val state: StateFlow<ServersViewState> = state_

    fun loadServers() {
        viewModelScope.launch(Dispatchers.IO) {
            state_.update {
                it.copy(
                    preInstalledServers = serversRepository.getPreInstalledServers()
                        .mapToServerItems(),
                    preinstalledStages = stagesRepository.getPreInstalledStages().mapToStageItems(),
                    addedServers = serversRepository.getServers().mapToServerItems(),
                    addedStages = stagesRepository.getStages().mapToStageItems()
                )
            }
        }
    }

    fun onAddClicked() {
        state_.update {
            it.copy(serverDialogState = it.serverDialogState.copy(show = true))
        }
    }

    fun dismissDialogs() {
        state_.update {
            it.copy(serverDialogState = ServerDialogState())
        }
    }

    fun onServerNameChanged(name: String) {
        state_.update {
            it.copy(serverDialogState = it.serverDialogState.copy(serverName = name))
        }
    }

    fun onServerUrlChanged(url: String) {
        state_.update {
            it.copy(serverDialogState = it.serverDialogState.copy(serverUrl = url))
        }
    }

    fun onSaveServerClicked() {
        if (allServerFieldValid()) {
            val dialogState = state_.value.serverDialogState

            if (dialogState.editableServerId == null) {
                addServer(dialogState.serverName, dialogState.serverUrl)
            } else {
                updateServerData(
                    dialogState.editableServerId,
                    dialogState.serverName,
                    dialogState.serverUrl
                )
            }

            state_.update {
                it.copy(serverDialogState = ServerDialogState())
            }
        }
    }

    private fun allServerFieldValid(): Boolean {
        val dialogState = state_.value.serverDialogState
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

        state_.update {
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
            state_.update {
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
            state_.update {
                it.copy(
                    addedServers = serversRepository.getServers().mapToServerItems()
                )
            }
        }
    }

    private fun updateServerData(id: Int, name: String, url: String) {
        val serverForUpdate = state_.value.addedServers
            .find { it.server.id == id }
            ?.server

        serverForUpdate?.let {
            val updatedServer = serverForUpdate.copy(name = name, url = url)
            viewModelScope.safeLaunch {
                serversRepository.updateServer(updatedServer)
                state_.update {
                    it.copy(addedServers = serversRepository.getServers().mapToServerItems())
                }
            }
        }
    }

    fun onEditServerClicked(debugServer: DebugServer) {
        state_.update {
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
        val selectedServer = serversRepository.getSelectedServer()
        if (debugServer != selectedServer) {
            getPlugin<ServersPlugin>().pushEvent(ServerSelectedEvent(debugServer))
            serversRepository.saveSelectedServer(debugServer)
            loadServers()
        }
    }

    fun onStageClicked(debugStage: DebugStage) {
        val selectedStage = stagesRepository.getSelectedStage()
        if (debugStage != selectedStage) {
            stagesRepository.saveSelectedStage(debugStage)
            getPlugin<ServersPlugin>().pushEvent(StageSelectedEvent(debugStage))
            loadServers()
        }
    }

    private fun List<DebugServer>.mapToServerItems(): List<ServerItemData> {
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
