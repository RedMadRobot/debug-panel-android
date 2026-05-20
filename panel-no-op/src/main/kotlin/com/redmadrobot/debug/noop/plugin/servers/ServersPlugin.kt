package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import java.util.Collections.emptyList

/**
 * No-op implementation of [ServersPlugin] for release builds.
 *
 * Performs no actions; only mirrors the public constructor signatures.
 */
public class ServersPlugin(
    private val preInstalledServers: List<Any> = emptyList()
) {
    public constructor(debugDataProvider: DebugDataProvider<List<DebugServer>>) : this(
        preInstalledServers = debugDataProvider.provideData()
    )
}
