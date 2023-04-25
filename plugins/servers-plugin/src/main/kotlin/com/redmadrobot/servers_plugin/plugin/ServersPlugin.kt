package com.redmadrobot.servers_plugin.plugin

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.ui.ServersFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public class ServersPlugin(
    private val preInstalledServers: List<DebugServer> = emptyList()
) : Plugin() {

    init {
        preInstalledServers.find { it.isDefault }
            ?: throw IllegalStateException("DebugPanel - ServersPlugin can't be initialized. At least one server must be default")
    }

    public companion object {
        internal const val NAME = "SERVERS"

        public fun getSelectedServer(): Flow<DebugServer> {
            return flow {
                emit(
                    getPlugin<ServersPlugin>()
                        .getContainer<ServersPluginContainer>()
                        .serversRepository
                        .getSelectedServer()
                )
            }
        }

        public fun getDefaultServer(): DebugServer {
            return getPlugin<ServersPlugin>()
                .getContainer<ServersPluginContainer>()
                .serversRepository
                .getDefault()
        }
    }

    public constructor(preInstalledServers: DebugDataProvider<List<DebugServer>>) : this(
        preInstalledServers = preInstalledServers.provideData()
    )

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return ServersPluginContainer(preInstalledServers, commonContainer)
    }

    override fun getFragment(): Fragment {
        return ServersFragment().apply {
            arguments = bundleOf(ServersFragment.IS_EDIT_MODE_KEY to false)
        }
    }

    override fun getSettingFragment(): Fragment {
        return ServersFragment().apply {
            arguments = bundleOf(ServersFragment.IS_EDIT_MODE_KEY to true)
        }
    }
}
