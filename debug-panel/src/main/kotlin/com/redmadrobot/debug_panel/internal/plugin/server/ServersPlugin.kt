package com.redmadrobot.debug_panel.internal.plugin.server

import androidx.fragment.app.Fragment
import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.Plugin
import com.redmadrobot.core.PluginDependencyContainer
import com.redmadrobot.core.data.PreInstalledData
import com.redmadrobot.core.data.storage.entity.DebugServer
import com.redmadrobot.debug_panel.ui.servers.add.ServersFragment
import com.redmadrobot.debug_panel.ui.servers.choose.ServerSelectionFragment

class ServersPlugin(
    private val preInstalledServers: PreInstalledData<DebugServer> = PreInstalledData(
        emptyList()
    )
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
