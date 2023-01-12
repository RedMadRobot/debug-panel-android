package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.servers.data.DebugServerRepository
import com.redmadrobot.debug.plugin.servers.data.DebugStageRepository
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.model.DebugServerData
import com.redmadrobot.debug.plugin.servers.data.model.DebugStage
import com.redmadrobot.debug.plugin.servers.data.storage.ServersPluginDatabase
import com.redmadrobot.debug.plugin.servers.ui.ServersViewModel

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
            container.context,
            serversRepository,
            stagesRepository
        )
    }
}
