package com.redmadrobot.debug.servers.plugin

import com.redmadrobot.debug.core.CommonContainer
import com.redmadrobot.debug.core.plugin.PluginDependencyContainer
import com.redmadrobot.debug.servers.data.DebugServerRepository
import com.redmadrobot.debug.servers.data.DebugStageRepository
import com.redmadrobot.debug.servers.data.model.DebugServer
import com.redmadrobot.debug.servers.data.model.DebugServerData
import com.redmadrobot.debug.servers.data.model.DebugStage
import com.redmadrobot.debug.servers.data.storage.ServersPluginDatabase
import com.redmadrobot.debug.servers.ui.ServersViewModel

internal class ServersPluginContainer(
    private val preinstalledStages: List<DebugServerData>,
    private val container: CommonContainer
) : PluginDependencyContainer {

    private val pluginStorage by lazy { ServersPluginDatabase.getInstance(container.context) }

    val serversRepository by lazy {
        DebugServerRepository(
            container.context,
            pluginStorage.getDebugServersDao(),
            preinstalledStages.filterIsInstance<DebugServer>(),
        )
    }

    val stagesRepository by lazy {
        DebugStageRepository(
            container.context,
            pluginStorage.getDebugStagesDao(),
            preinstalledStages.filterIsInstance<DebugStage>()
        )
    }

    fun createServersViewModel(): ServersViewModel {
        return ServersViewModel(
            serversRepository,
            stagesRepository
        )
    }
}
