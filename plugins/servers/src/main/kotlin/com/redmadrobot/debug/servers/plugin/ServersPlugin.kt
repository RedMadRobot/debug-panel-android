package com.redmadrobot.debug.servers.plugin

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.core.CommonContainer
import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.plugin.PluginDependencyContainer
import com.redmadrobot.debug.servers.data.model.DebugServer
import com.redmadrobot.debug.servers.ui.ServersFragment
import kotlinx.coroutines.runBlocking

public class ServersPlugin(
    private val preInstalledServers: List<DebugServer> = emptyList()
) : Plugin() {

    init {
        preInstalledServers.find { it.isDefault }
            ?: throw IllegalStateException("DebugPanel - ServersPlugin can't be initialized. At least one server must be default")
    }

    public companion object {
        internal const val NAME = "SERVERS"

        public fun getSelectedServer(): DebugServer {
            return runBlocking {
                getPlugin<ServersPlugin>()
                    .getContainer<ServersPluginContainer>()
                    .serversRepository
                    .getSelectedServer()
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
