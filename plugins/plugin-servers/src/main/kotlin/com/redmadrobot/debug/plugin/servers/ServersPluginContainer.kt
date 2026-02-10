package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.servers.data.DebugServerRepository
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.storage.ServersPluginDatabase
import com.redmadrobot.debug.plugin.servers.ui.ServersViewModel

internal class ServersPluginContainer(
    private val preinstalledServers: List<DebugServer>,
    private val container: CommonContainer
) : PluginDependencyContainer {
    private val pluginStorage by lazy { ServersPluginDatabase.getInstance(container.context) }

    val serversRepository by lazy {
        DebugServerRepository(
            context = container.context,
            debugServersDao = pluginStorage.getDebugServersDao(),
            preInstalledServers = preinstalledServers,
        )
    }

    fun createServersViewModel(): ServersViewModel {
        return ServersViewModel(
            serversRepository,
        )
    }
}
