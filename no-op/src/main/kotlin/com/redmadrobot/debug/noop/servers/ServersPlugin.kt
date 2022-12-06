package com.redmadrobot.debug.servers.plugin

import com.redmadrobot.debug.servers.data.model.DebugServer
import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import java.util.Collections.emptyList

class ServersPlugin(
    private val preInstalledServers: List<Any> = emptyList()
){
    constructor(debugDataProvider: DebugDataProvider<List<DebugServer>>) : this(
        preInstalledServers = debugDataProvider.provideData()
    )
}
