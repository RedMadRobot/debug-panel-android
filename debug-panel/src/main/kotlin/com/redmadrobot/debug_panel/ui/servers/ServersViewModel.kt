package com.redmadrobot.debug_panel.ui.servers

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.core.data.servers.DebugServerRepository
import com.redmadrobot.core.data.storage.PanelSettingsRepository
import com.redmadrobot.core.data.storage.entity.DebugServer
import com.redmadrobot.core.extension.observeOnMain
import com.redmadrobot.core.ui.base.BaseViewModel
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.ui.servers.item.DebugServerItem
import com.redmadrobot.debug_panel.ui.view.SectionHeaderItem
import io.reactivex.rxkotlin.subscribeBy

class ServersViewModel(
    private val context: Context,
    private val serversRepository: DebugServerRepository,
    private val panelSettingsRepository: PanelSettingsRepository
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

    fun addServer(host: String) {
        val server =
            DebugServer(url = host)
        serversRepository.addServer(server)
            .observeOnMain()
            .subscribeBy(onComplete = {
                loadAddedServers()
            })
            .autoDispose()
    }

    fun removeServer(serverItem: DebugServerItem) {
        serversRepository.removeServer(serverItem.debugServer)
            .observeOnMain()
            .subscribeBy(
                onComplete = {
                    loadAddedServers()
                },
                onError = {
                    //TODO логирование ошибки
                }
            )
            .autoDispose()
    }

    fun updateServerData(oldValue: String, newValue: String) {
        val itemForUpdate = state.value?.addedItems
            ?.find { it is DebugServerItem && it.debugServer.url == oldValue } as? DebugServerItem

        val serverForUpdate = itemForUpdate?.debugServer
        val updatedServer = serverForUpdate?.copy(url = newValue)

        updatedServer?.let { server ->
            serversRepository.updateServer(server)
                .observeOnMain()
                .subscribeBy(
                    onComplete = {
                        itemForUpdate.update(server)
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


    private fun loadPreInstalledServers() {
        serversRepository.getPreInstalledServers()
            .map { addDefaultServer(it) }
            .filter { it.isNotEmpty() }
            .map { servers ->
                listOf(SectionHeaderItem(context.getString(R.string.pre_installed)))
                    .plus(mapToItems(servers))
            }
            .observeOnMain()
            .subscribeBy(onSuccess = { items ->
                state.value = state.value?.copy(preInstalledItems = items)
            })
            .autoDispose()
    }

    private fun loadAddedServers() {
        serversRepository.getServers()
            .filter { it.isNotEmpty() }
            .map { servers ->
                listOf(SectionHeaderItem(context.getString(R.string.added)))
                    .plus(mapToItems(servers))
            }
            .observeOnMain()
            .subscribeBy(onSuccess = { items ->
                state.value = state.value?.copy(addedItems = items)
            })
            .autoDispose()
    }

    private fun addDefaultServer(servers: List<DebugServer>): List<DebugServer> {
        val defaultServer = DebugServer.getEmpty()
        return listOf(defaultServer).plus(servers)
    }

    private fun mapToItems(servers: List<DebugServer>): List<DebugServerItem> {
        val selectedHost = panelSettingsRepository.getSelectedServerHost()
        return servers.map { debugServer ->
            val isSelected = selectedHost != null && selectedHost == debugServer.url
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
