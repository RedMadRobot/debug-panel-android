package com.redmadrobot.servers_plugin.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel_core.extension.observeOnMain
import com.redmadrobot.debug_panel_core.ui.SectionHeaderItem
import com.redmadrobot.debug_panel_core.ui.base.BaseViewModel
import com.redmadrobot.servers_plugin.R
import com.redmadrobot.servers_plugin.data.DebugServerRepository
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.data.repository.PluginSettingsRepository
import com.redmadrobot.servers_plugin.ui.item.DebugServerItem
import timber.log.Timber

class ServersViewModel(
    private val context: Context,
    private val serversRepository: DebugServerRepository,
    private val pluginSettingsRepository: PluginSettingsRepository
) : BaseViewModel() {

    val state = MutableLiveData<ServersViewState>().apply {
        /*Default state*/
        value = ServersViewState(
            preInstalledItems = emptyList(),
            addedItems = emptyList()
        )
    }
    private var selectedServerItem: DebugServerItem? = null

    fun loadServers() {
        loadPreInstalledServers()
        loadAddedServers()
    }

    fun addServer(name: String, url: String) {
        val server = DebugServer(name = name, url = url)
        serversRepository.addServer(server)
            .observeOnMain()
            .subscribe(
                { loadAddedServers() },
                { Timber.e(it) }
            )
            .autoDispose()
    }

    fun removeServer(serverItem: DebugServerItem) {
        serversRepository.removeServer(serverItem.debugServer)
            .observeOnMain()
            .subscribe(
                { loadAddedServers() },
                { Timber.e(it) }
            )
            .autoDispose()
    }

    fun updateServerData(id: Int, name: String, url: String) {
        val itemForUpdate = state.value?.addedItems
            ?.find { it is DebugServerItem && it.debugServer.id == id } as? DebugServerItem

        val serverForUpdate = itemForUpdate?.debugServer
        val updatedServer = serverForUpdate?.copy(name = name, url = url)

        updatedServer?.let { server ->
            serversRepository.updateServer(server)
                .observeOnMain()
                .subscribe(
                    { itemForUpdate.update(server) },
                    { Timber.e(it) }
                )
                .autoDispose()
        }
    }

    fun selectServerAsCurrent(debugServerItem: DebugServerItem) {
        updateSelectedItem(debugServerItem)
        val serverData = debugServerItem.debugServer
        pluginSettingsRepository.saveSelectedServer(serverData)
    }


    private fun loadPreInstalledServers() {
        serversRepository.getPreInstalledServers()
            .map { addDefaultServer(it) }
            .filter { it.isNotEmpty() }
            .map { servers ->
                listOf(
                    SectionHeaderItem(context.getString(R.string.pre_installed_servers))
                )
                    .plus(mapToItems(servers))
            }
            .observeOnMain()
            .subscribe(
                { items ->
                    state.value = state.value?.copy(preInstalledItems = items)
                },
                { Timber.e(it) }
            )
            .autoDispose()
    }

    private fun loadAddedServers() {
        serversRepository.getServers()
            .filter { it.isNotEmpty() }
            .map { servers ->
                val headerText = context.getString(R.string.added_servers)
                listOf(SectionHeaderItem(headerText))
                    .plus(mapToItems(servers))
            }
            .observeOnMain()
            .subscribe(
                { items ->
                    state.value = state.value?.copy(addedItems = items)
                },
                { Timber.e(it) }
            )
            .autoDispose()
    }

    private fun addDefaultServer(servers: List<DebugServer>): List<DebugServer> {
        val defaultServer = DebugServer.getEmpty()
        return listOf(defaultServer).plus(servers)
    }

    private fun mapToItems(servers: List<DebugServer>): List<DebugServerItem> {
        val selectedServer = pluginSettingsRepository.getSelectedServer()
        return servers.map { debugServer ->
            val isSelected = selectedServer != null && selectedServer.url == debugServer.url
            DebugServerItem(debugServer, isSelected).also { item ->
                if (isSelected) this.selectedServerItem = item
            }
        }
    }

    private fun updateSelectedItem(debugServerItem: DebugServerItem) {
        this.selectedServerItem?.isSelected = false
        this.selectedServerItem?.notifyChanged()

        debugServerItem.isSelected = true
        debugServerItem.notifyChanged()

        this.selectedServerItem = debugServerItem
    }
}
