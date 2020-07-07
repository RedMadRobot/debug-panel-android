package com.redmadrobot.servers_plugin.plugin

import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.data.PreInstalledData
import com.redmadrobot.core.data.storage.entity.DebugServer
import com.redmadrobot.core.plugin.PluginDependencyContainer
import com.redmadrobot.servers_plugin.data.LocalDebugServerRepository
import com.redmadrobot.servers_plugin.ui.ServersViewModel

internal class ServersPluginContainer(
    private val preInstalledServers: PreInstalledData<DebugServer>,
    private val container: CommonContainer
) : PluginDependencyContainer {

    private val serversRepository by lazy {
        LocalDebugServerRepository(
            container.dataBaseInstance.getDebugServersDao(),
            preInstalledServers
        )
    }

    fun createServersViewModel(): ServersViewModel {
        return ServersViewModel(
            container.context,
            serversRepository,
            container.panelSettingsRepository
        )
    }
}
