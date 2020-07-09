package com.redmadrobot.servers_plugin.plugin

import androidx.fragment.app.Fragment
import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.data.PreInstalledData
import com.redmadrobot.core.plugin.Plugin
import com.redmadrobot.core.plugin.PluginDependencyContainer
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.listener.DefaultOnServerChangedListener
import com.redmadrobot.servers_plugin.listener.OnServerChangedListener
import com.redmadrobot.servers_plugin.ui.add.ServersFragment
import com.redmadrobot.servers_plugin.ui.choose.ServerSelectionFragment

class ServersPlugin(
    private val preInstalledServers: PreInstalledData<DebugServer> = PreInstalledData(emptyList()),
    val onServerChangedListener: OnServerChangedListener = DefaultOnServerChangedListener()
) : Plugin() {

    companion object {
        const val NAME = "SERVERS"
    }

    override fun getName() = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return ServersPluginContainer(preInstalledServers, commonContainer)
    }

    override fun getFragment(): Fragment? {
        return ServerSelectionFragment()
    }

    override fun getSettingFragment(): Fragment? {
        return ServersFragment()
    }
}
