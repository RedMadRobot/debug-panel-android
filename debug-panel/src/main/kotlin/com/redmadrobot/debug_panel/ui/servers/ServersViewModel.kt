package com.redmadrobot.debug_panel.ui.servers

import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel.data.servers.DebugServerRepository
import com.redmadrobot.debug_panel.data.storage.PanelSettingsRepository
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import com.redmadrobot.debug_panel.extension.observeOnMain
import com.redmadrobot.debug_panel.extension.zipList
import com.redmadrobot.debug_panel.ui.base.BaseViewModel
import com.redmadrobot.debug_panel.ui.servers.item.DebugServerItem
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.rxkotlin.subscribeBy

class ServersViewModel(
    private val serversRepository: DebugServerRepository,
    private val panelSettingsRepository: PanelSettingsRepository
) : BaseViewModel() {

    val servers = MutableLiveData<List<Item>>()
    private var selectedServerItem: DebugServerItem? = null

    fun loadServers() {
        serversRepository.getPreInstalledServers()
            .zipList(serversRepository.getServers())
            .map { mapToItems(it) }
            .observeOnMain()
            .subscribeBy(onSuccess = { servers.value = it })
            .autoDispose()
    }

    private fun mapToItems(servers: List<DebugServer>): List<DebugServerItem> {
        val selectedHost = panelSettingsRepository.getSelectedServerHost()
        return servers.map { debugServer ->
            val isSelected = selectedHost != null && selectedHost == debugServer.url
            DebugServerItem(debugServer, isSelected).also {
                if (isSelected) this.selectedServerItem = it
            }
        }
    }

    fun addServer(host: String) {
        val server = DebugServer(url = host)
        serversRepository.addServer(server)
            .observeOnMain()
            .subscribeBy(onComplete = {
                addServerToEndOfList(server)
            })
            .autoDispose()
    }

    fun removeServer(position: Int) {
        val server = servers.value?.get(position) as? DebugServerItem
        server?.let {
            serversRepository.removeServer(it.debugServer)
                .observeOnMain()
                .subscribeBy(onComplete = {
                    removeServerByPosition(position)
                })
                .autoDispose()
        }
    }

    fun updateServerHost(oldValue: String, newValue: String) {
        val itemForUpdate = servers.value
            ?.map { it as DebugServerItem }
            ?.find { it.debugServer.url == oldValue }

        itemForUpdate?.let { item ->
            val serverForUpdate = item.debugServer
            val updatedServer = serverForUpdate.copy(url = newValue)

            serversRepository.updateServer(updatedServer)
                .observeOnMain()
                .subscribeBy(
                    onComplete = {
                        item.update(updatedServer)
                    }
                )
                .autoDispose()
        }
    }

    fun selectServerAsCurrent(debugServerItem: DebugServerItem) {
        updateSelectedItem(debugServerItem)
        val serverData = debugServerItem.debugServer
        panelSettingsRepository.saveSelectedServerHost(serverData.url)
    }

    private fun removeServerByPosition(position: Int) {
        val serverList = servers.value?.toMutableList()
        serverList?.removeAt(position)
        servers.value = serverList
    }

    private fun addServerToEndOfList(server: DebugServer) {
        val serverList = servers.value?.toMutableList()
        serverList?.add(DebugServerItem(server, false))
        servers.value = serverList
    }

    private fun updateSelectedItem(debugServerItem: DebugServerItem) {
        this.selectedServerItem?.isSelected = false
        this.selectedServerItem?.notifyChanged()

        debugServerItem.isSelected = true
        debugServerItem.notifyChanged()

        this.selectedServerItem = debugServerItem
    }
}
