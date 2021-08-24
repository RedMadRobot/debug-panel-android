package com.redmadrobot.servers_plugin.plugin

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.ui.add.ServersFragment

public class ServersPlugin(
    private val preInstalledServers: List<DebugServer> = emptyList()
) : Plugin() {

    internal companion object {
        const val NAME = "SERVERS"
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
