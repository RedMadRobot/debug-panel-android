package com.redmadrobot.debug.plugin.servers

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.EditablePlugin
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.ui.ServersScreen
import kotlinx.coroutines.runBlocking

public class ServersPlugin(
    private val preInstalledServers: List<DebugServer> = emptyList(),
) : Plugin(), EditablePlugin {

    init {
        preInstalledServers.find { it.isDefault }
            ?: error("ServersPlugin can't be initialized. At least one server must be default")
    }

    public companion object {
        internal const val NAME = "SERVERS"

        public fun getSelectedServer(): DebugServer {
            return runBlocking {
                getPlugin<ServersPlugin>().getContainer<ServersPluginContainer>().serversRepository.getSelectedServer()
            }
        }

        public fun getDefaultServer(): DebugServer {
            return getPlugin<ServersPlugin>().getContainer<ServersPluginContainer>().serversRepository.getDefault()
        }
    }

    public constructor(preInstalledServers: DebugDataProvider<List<DebugServer>>) : this(
        preInstalledServers = preInstalledServers.provideData()
    )

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return ServersPluginContainer(preInstalledServers, commonContainer)
    }

    @Composable
    override fun content() {
        ServersScreen(isEditMode = false)
    }

    @Composable
    override fun settingsContent() {
        ServersScreen(isEditMode = true)
    }
}
