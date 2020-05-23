package com.redmadrobot.debug_panel.internal.plugin.server

import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel.data.PreInstalledData
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import com.redmadrobot.debug_panel.internal.CommonContainer
import com.redmadrobot.debug_panel.internal.plugin.Plugin
import com.redmadrobot.debug_panel.internal.plugin.PluginDependencyContainer
import com.redmadrobot.debug_panel.ui.servers.add.ServersFragment
import com.redmadrobot.debug_panel.ui.servers.choose.ServerSelectionFragment

class ServersPlugin(
    private val preInstalledServers: PreInstalledData<DebugServer> = PreInstalledData(emptyList())
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
