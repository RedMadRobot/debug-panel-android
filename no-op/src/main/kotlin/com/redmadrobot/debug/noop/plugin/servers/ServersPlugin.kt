package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import java.util.Collections.emptyList

class ServersPlugin(
    private val preInstalledServers: List<Any> = emptyList()
) {
    constructor(debugDataProvider: DebugDataProvider<List<DebugServer>>) : this(
        preInstalledServers = debugDataProvider.provideData()
    )
}
