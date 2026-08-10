package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer

/**
 * No-op implementation of [ServersPlugin] for release builds.
 *
 * Performs no actions; only mirrors the public constructor signatures.
 */
@Suppress("UnusedPrivateProperty")
public class ServersPlugin(
    private val preInstalledServers: List<DebugServer> = emptyList(),
) {
    public constructor(preInstalledServers: DebugDataProvider<List<DebugServer>>) : this(
        preInstalledServers = preInstalledServers.provideData()
    )

    public companion object {
        /**
         * Always throws: no plugin is registered in release builds.
         *
         * Mirrors the real implementation, which throws the same exception when [ServersPlugin]
         * is not registered in the panel. Guard the call with `DebugPanel.isInitialized` or use
         * the application's own configuration in release builds.
         *
         * @throws IllegalArgumentException always
         */
        public fun getSelectedServer(): DebugServer = noPlugin()

        /**
         * Always throws: no plugin is registered in release builds.
         *
         * @throws IllegalArgumentException always
         * @see getSelectedServer
         */
        public fun getDefaultServer(): DebugServer = noPlugin()

        private fun noPlugin(): Nothing {
            throw IllegalArgumentException("ServersPlugin is not available in release builds")
        }
    }
}
