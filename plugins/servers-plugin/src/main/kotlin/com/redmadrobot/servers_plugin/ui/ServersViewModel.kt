package com.redmadrobot.servers_plugin.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.debug_panel_common.extension.safeLaunch
import com.redmadrobot.servers_plugin.R
import com.redmadrobot.servers_plugin.data.DebugServerRepository
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.ui.item.DebugServerItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ServersViewModel(
    private val context: Context,
    private val serversRepository: DebugServerRepository
) : PluginViewModel() {

    val state = MutableLiveData<ServersViewState>().apply {
        /*Default state*/
        value = ServersViewState(
            preInstalledServers = emptyList(),
            addedServers = emptyList()
        )
    }

    fun loadServers() {
        viewModelScope.safeLaunch {
            withContext(Dispatchers.IO) {
                loadPreInstalledServers()
                loadAddedServers()
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
        val itemForUpdate = state.value?.addedServers
            ?.find { it is DebugServerItems.AddedServer && it.debugServer.id == id }
                as? DebugServerItems.AddedServer

        val serverForUpdate = itemForUpdate?.debugServer
        val updatedServer = serverForUpdate?.copy(name = name, url = url)

        updatedServer?.let { server ->
            viewModelScope.safeLaunch {
                serversRepository.updateServer(server)
                loadAddedServers()
            }
        }
    }

    fun selectServerAsCurrent(debugServer: DebugServer) {
        serversRepository.saveSelectedServer(debugServer)
        loadServers()
    }

    private suspend fun loadPreInstalledServers() {
        val servers = serversRepository.getPreInstalledServers()
        val headerText = context.getString(R.string.pre_installed_servers)
        val serverItems = mapToPreinstalledItems(headerText, servers)
        withContext(Dispatchers.Main) {
            state.value = state.value?.copy(preInstalledServers = serverItems)
        }
    }

    private suspend fun loadAddedServers() {
        val servers = serversRepository.getServers()
        val headerText = context.getString(R.string.added_servers)
        val serverItems = mapToAddedItems(headerText, servers)
        withContext(Dispatchers.Main) {
            state.value = state.value?.copy(addedServers = serverItems)
        }
    }

    private fun mapToPreinstalledItems(
        header: String,
        servers: List<DebugServer>
    ): List<DebugServerItems> {
        val selectedServer = serversRepository.getSelectedServer()
        val items = servers.map { debugServer ->
            val isSelected = selectedServer.url == debugServer.url
            DebugServerItems.PreinstalledServer(debugServer, isSelected)
        }

        return listOf(/*Заголовок списка*/DebugServerItems.Header(header)).plus(items)
    }

    private fun mapToAddedItems(
        header: String,
        servers: List<DebugServer>
    ): List<DebugServerItems> {
        if (servers.isEmpty()) return emptyList()
        val selectedServer = serversRepository.getSelectedServer()
        val items = servers.map { debugServer ->
            val isSelected = selectedServer.url == debugServer.url
            DebugServerItems.AddedServer(debugServer, isSelected)
        }

        return listOf(/*Заголовок списка*/DebugServerItems.Header(header)).plus(items)
    }
}
