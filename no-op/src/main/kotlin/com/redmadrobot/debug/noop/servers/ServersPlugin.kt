package com.redmadrobot.servers_plugin.plugin

import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.servers_plugin.data.model.DebugServer
import java.util.Collections.emptyList

class ServersPlugin(
    private val preInstalledServers: List<Any> = emptyList()
){
    constructor(debugDataProvider: DebugDataProvider<List<DebugServer>>) : this(
        preInstalledServers = debugDataProvider.provideData()
    )
}
