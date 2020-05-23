package com.redmadrobot.debug_panel.internal.plugin.server

import com.redmadrobot.debug_panel.data.PreInstalledData
import com.redmadrobot.debug_panel.data.servers.LocalDebugServerRepository
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import com.redmadrobot.debug_panel.internal.CommonContainer
import com.redmadrobot.debug_panel.internal.plugin.PluginDependencyContainer
import com.redmadrobot.debug_panel.ui.servers.ServersViewModel

internal class ServersPluginContainer(
    private val preInstalledServers: PreInstalledData<DebugServer>,
    private val container: CommonContainer
) : PluginDependencyContainer {

    val serversRepository by lazy {
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
