package com.redmadrobot.servers_plugin.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.debug_panel_common.extension.safeLaunch
import com.redmadrobot.debug_panel_common.ui.SectionHeaderItem
import com.redmadrobot.servers_plugin.R
import com.redmadrobot.servers_plugin.data.DebugServerRepository
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.data.repository.PluginSettingsRepository
import com.redmadrobot.servers_plugin.ui.item.DebugServerItem
import com.xwray.groupie.kotlinandroidextensions.Item

internal class ServersViewModel(
    private val context: Context,
    private val serversRepository: DebugServerRepository,
    private val pluginSettingsRepository: PluginSettingsRepository
) : PluginViewModel() {

    val state = MutableLiveData<ServersViewState>().apply {
        /*Default state*/
        value = ServersViewState(
            preInstalledItems = emptyList(),
            addedItems = emptyList()
        )
    }
    private var selectedServerItem: DebugServerItem? = null

    fun loadServers() {
        viewModelScope.safeLaunch {
            loadPreInstalledServers()
            loadAddedServers()
        }
    }

    fun addServer(name: String, url: String) {
        val server = DebugServer(name = name, url = url)
        viewModelScope.safeLaunch {
            serversRepository.addServer(server)
            loadAddedServers()
        }
    }

    fun removeServer(serverItem: DebugServerItem) {
        viewModelScope.safeLaunch {
            serversRepository.removeServer(serverItem.debugServer)
            loadAddedServers()
        }
    }

    fun updateServerData(id: Int, name: String, url: String) {
        val itemForUpdate = state.value?.addedItems
            ?.find { it is DebugServerItem && it.debugServer.id == id } as? DebugServerItem

        val serverForUpdate = itemForUpdate?.debugServer
        val updatedServer = serverForUpdate?.copy(name = name, url = url)

        updatedServer?.let { server ->
            viewModelScope.safeLaunch {
                serversRepository.updateServer(server)
                itemForUpdate.update(server)
            }
        }
    }

    fun selectServerAsCurrent(debugServerItem: DebugServerItem) {
        updateSelectedItem(debugServerItem)
        val serverData = debugServerItem.debugServer
        pluginSettingsRepository.saveSelectedServer(serverData)
    }


    private suspend fun loadPreInstalledServers() {
        val servers = serversRepository.getPreInstalledServers()
        val serverItems = mapToItems(context.getString(R.string.pre_installed_servers), servers)
        state.value = state.value?.copy(preInstalledItems = serverItems)
    }

    private suspend fun loadAddedServers() {
        val servers = serversRepository.getServers()
        if (servers.isNotEmpty()) {
            val serverItems = mapToItems(context.getString(R.string.added_servers), servers)
            state.value = state.value?.copy(addedItems = serverItems)
        }
    }

    private fun mapToItems(header: String, servers: List<DebugServer>): List<Item> {
        val selectedServer = pluginSettingsRepository.getSelectedServer()
        val items = servers.map { debugServer ->
            val isSelected = selectedServer.url == debugServer.url
            DebugServerItem(debugServer, isSelected).also { item ->
                if (isSelected) this.selectedServerItem = item
            }
        }
        return listOf(
            /*Заголовок списка*/
            SectionHeaderItem(header)
        ).plus(items)
    }

    private fun updateSelectedItem(debugServerItem: DebugServerItem) {
        this.selectedServerItem?.isSelected = false
        this.selectedServerItem?.notifyChanged()

        debugServerItem.isSelected = true
        debugServerItem.notifyChanged()

        this.selectedServerItem = debugServerItem
    }
}
