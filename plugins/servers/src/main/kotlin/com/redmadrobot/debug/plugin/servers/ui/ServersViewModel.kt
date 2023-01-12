package com.redmadrobot.debug.plugin.servers.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug.common.base.PluginViewModel
import com.redmadrobot.debug.common.extension.safeLaunch
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.plugin.servers.R
import com.redmadrobot.debug.plugin.servers.ServerSelectedEvent
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug.plugin.servers.StageSelectedEvent
import com.redmadrobot.debug.plugin.servers.data.DebugServerRepository
import com.redmadrobot.debug.plugin.servers.data.DebugStageRepository
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.model.DebugServerData
import com.redmadrobot.debug.plugin.servers.data.model.DebugStage
import com.redmadrobot.debug.plugin.servers.ui.item.DebugServerItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ServersViewModel(
    private val context: Context,
    private val serversRepository: DebugServerRepository,
    private val stagesRepository: DebugStageRepository
) : PluginViewModel() {

    private val _state = MutableLiveData(ServersViewState())
    val state: LiveData<List<DebugServerItems>>
        get() = _state.map {
            it.preinstalledStages
                .plus(it.addedStages)
                .plus(it.preInstalledServers)
                .plus(it.addedServers)
        }


    fun loadServers() {
        viewModelScope.safeLaunch {
            withContext(Dispatchers.IO) {
                loadPreInstalledServers()
                loadAddedServers()
                loadPreInstalledStages()
                loadAddedStages()
            }
        }
    }

    fun addServer(name: String, url: String) {
        val server = DebugServer(name = name, url = url, isDefault = false)
        viewModelScope.safeLaunch {
            serversRepository.addServer(server)
            loadAddedServers()
        }
    }

    fun removeServer(debugServer: DebugServer) {
        viewModelScope.safeLaunch {
            serversRepository.removeServer(debugServer)
            loadAddedServers()
        }
    }

    fun updateServerData(id: Int, name: String, url: String) {
        val itemForUpdate = _state.value?.addedServers
            ?.find { it is DebugServerItems.AddedServer && it.debugServer.id == id }
                as? DebugServerItems.AddedServer

        val serverForUpdate = itemForUpdate?.debugServer

        (serverForUpdate as? DebugServer)?.let {
            val updatedServer = serverForUpdate.copy(name = name, url = url)
            viewModelScope.safeLaunch {
                serversRepository.updateServer(updatedServer)
                loadAddedServers()
            }
        }
    }

    fun onServerSelected(debugServer: DebugServer) {
        viewModelScope.launch {
            val selectedServer = serversRepository.getSelectedServer()
            if (debugServer != selectedServer) {
                getPlugin<ServersPlugin>().pushEvent(ServerSelectedEvent(debugServer))
                serversRepository.saveSelectedServer(debugServer)
                loadServers()
            }
        }
    }

    fun onStageSelected(debugStage: DebugStage) {
        val selectedStage = stagesRepository.getSelectedStage()
        if (debugStage != selectedStage) {
            stagesRepository.saveSelectedStage(debugStage)
            getPlugin<ServersPlugin>().pushEvent(StageSelectedEvent(debugStage))
            loadServers()
        }
    }

    @Deprecated("Migration to the stages system. Please use loadPreInstalledStages()")
    private suspend fun loadPreInstalledServers() {
        val servers = serversRepository.getPreInstalledServers()
        val headerText = context.getString(R.string.pre_installed_servers)
        val serverItems = mapToPreinstalledItems(headerText, servers)
        withContext(Dispatchers.Main) {
            _state.value = _state.value?.copy(preInstalledServers = serverItems)
        }
    }

    private suspend fun loadPreInstalledStages() {
        val stages = stagesRepository.getPreInstalledStages()
        val headerText = context.getString(R.string.pre_installed_stages)
        val stageItems = mapToPreinstalledItems(headerText, stages)
        withContext(Dispatchers.Main) {
            _state.value = _state.value?.copy(preinstalledStages = stageItems)
        }
    }

    @Deprecated("Migration to the stages system. Please use loadAddedStages()")
    private suspend fun loadAddedServers() {
        val servers = serversRepository.getServers()
        val headerText = context.getString(R.string.added_servers)
        val serverItems = mapToAddedItems(headerText, servers)
        withContext(Dispatchers.Main) {
            _state.value = _state.value?.copy(addedServers = serverItems)
        }
    }

    private suspend fun loadAddedStages() {
        val stages = stagesRepository.getStages()
        val headerText = context.getString(R.string.added_servers)
        val serverItems = mapToAddedItems(headerText, stages)
        withContext(Dispatchers.Main) {
            _state.value = _state.value?.copy(addedStages = serverItems)
        }
    }

    private suspend fun mapToPreinstalledItems(
        header: String,
        servers: List<DebugServerData>
    ): List<DebugServerItems> {
        val selectedServer = serversRepository.getSelectedServer()
        val selectedStage = stagesRepository.getSelectedStage()

        val items = servers.map { debugServer ->
            val isSelected = when (debugServer) {
                is DebugServer -> selectedServer.url == debugServer.url
                is DebugStage -> selectedStage == debugServer
                else -> false
            }
            DebugServerItems.PreinstalledServer(debugServer, isSelected)
        }

        return listOf(DebugServerItems.Header(header)).plus(items)
    }

    private suspend fun mapToAddedItems(
        header: String,
        servers: List<DebugServerData>
    ): List<DebugServerItems> {
        if (servers.isEmpty()) return emptyList()
        val selectedServer = serversRepository.getSelectedServer()
        val selectedStage = stagesRepository.getSelectedStage()

        val items = servers.map { debugServer ->
            val isSelected = when (debugServer) {
                is DebugServer -> selectedServer.url == debugServer.url
                is DebugStage -> selectedStage == debugServer
                else -> false
            }
            DebugServerItems.AddedServer(debugServer, isSelected)
        }

        return listOf(DebugServerItems.Header(header)).plus(items)
    }

    private suspend fun getSelectedServer(): DebugServer {
        return serversRepository.getSelectedServer()
    }

}
