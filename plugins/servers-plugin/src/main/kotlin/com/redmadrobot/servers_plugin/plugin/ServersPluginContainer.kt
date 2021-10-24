package com.redmadrobot.servers_plugin.plugin

import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.servers_plugin.data.LocalDebugServerRepository
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.data.storage.ServersPluginDatabase
import com.redmadrobot.servers_plugin.ui.ServersViewModel

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
