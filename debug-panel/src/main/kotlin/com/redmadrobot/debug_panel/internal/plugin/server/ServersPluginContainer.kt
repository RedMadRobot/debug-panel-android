package com.redmadrobot.debug_panel.internal.plugin.server

import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.PluginDependencyContainer
import com.redmadrobot.core.data.PreInstalledData
import com.redmadrobot.core.data.servers.LocalDebugServerRepository
import com.redmadrobot.core.data.storage.entity.DebugServer
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
