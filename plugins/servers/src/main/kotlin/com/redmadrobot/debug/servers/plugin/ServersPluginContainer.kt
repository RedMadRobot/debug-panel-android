package com.redmadrobot.debug.servers.plugin

import com.redmadrobot.debug.core.CommonContainer
import com.redmadrobot.debug.core.plugin.PluginDependencyContainer
import com.redmadrobot.debug.servers.data.LocalDebugServerRepository
import com.redmadrobot.debug.servers.data.model.DebugServer
import com.redmadrobot.debug.servers.data.storage.ServersPluginDatabase
import com.redmadrobot.debug.servers.ui.ServersViewModel

internal class ServersPluginContainer(
    private val preInstalledServers: List<DebugServer>,
    private val container: CommonContainer
) : PluginDependencyContainer {

    private val pluginStorage by lazy { ServersPluginDatabase.getInstance(container.context) }

    val serversRepository by lazy {
        LocalDebugServerRepository(
            container.context,
            pluginStorage.getDebugServersDao(),
            preInstalledServers
        )
    }

    fun createServersViewModel(): ServersViewModel {
        return ServersViewModel(
            container.context,
            serversRepository
        )
    }
}
