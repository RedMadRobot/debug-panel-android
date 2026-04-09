package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.servers.data.DebugServerRepository
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.storage.ServersDataStore
import com.redmadrobot.debug.plugin.servers.ui.ServersViewModel

internal class ServersPluginContainer(
    private val preinstalledServers: List<DebugServer>,
    private val container: CommonContainer
) : PluginDependencyContainer {
    private val serversDataStore by lazy { ServersDataStore(container.context) }

    val serversRepository by lazy {
        DebugServerRepository(
            serversDataStore = serversDataStore,
            preInstalledServers = preinstalledServers,
        )
    }

    fun createServersViewModel(isEditMode: Boolean): ServersViewModel {
        return ServersViewModel(
            isEditMode = isEditMode,
            serversRepository = serversRepository,
        )
    }
}
