package com.redmadrobot.servers_plugin.plugin

import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.data.PreInstalledData
import com.redmadrobot.core.plugin.PluginDependencyContainer
import com.redmadrobot.servers_plugin.data.LocalDebugServerRepository
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.data.repository.PluginSettingsRepository
import com.redmadrobot.servers_plugin.data.storage.ServersPluginDatabase
import com.redmadrobot.servers_plugin.ui.ServersViewModel

internal class ServersPluginContainer(
    private val preInstalledServers: PreInstalledData<DebugServer>,
    private val container: CommonContainer
) : PluginDependencyContainer {

    val pluginSettingsRepository by lazy {
        PluginSettingsRepository(container.context)
    }

    private val pluginStorage by lazy { ServersPluginDatabase.getInstance(container.context) }

    private val serversRepository by lazy {
        LocalDebugServerRepository(
            pluginStorage.getDebugServersDao(),
            preInstalledServers
        )
    }

    fun createServersViewModel(): ServersViewModel {
        return ServersViewModel(
            container.context,
            serversRepository,
            pluginSettingsRepository
        )
    }
}
