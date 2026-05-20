package com.redmadrobot.debug.plugin.servers

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.EditablePlugin
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.ui.ServersScreen
import kotlinx.coroutines.runBlocking

/**
 * Plugin for switching between servers (environments) at runtime.
 *
 * When a server is selected, a [ServerSelectedEvent] is emitted.
 * For automatic URL substitution in OkHttp, use
 * [com.redmadrobot.debug.plugin.servers.interceptor.DebugServerInterceptor].
 *
 * At least one server in the list must be marked as [DebugServer.isDefault].
 *
 * @param preInstalledServers list of pre-installed servers
 * @throws IllegalStateException if no server is marked as default
 *
 * @see com.redmadrobot.debug.plugin.servers.interceptor.DebugServerInterceptor
 * @see ServerSelectedEvent
 */
public class ServersPlugin(
    private val preInstalledServers: List<DebugServer> = emptyList(),
) : Plugin(), EditablePlugin {
    init {
        preInstalledServers.find { it.isDefault }
            ?: error("ServersPlugin can't be initialized. At least one server must be default")
    }

    /**
     * Alternative constructor accepting a [DebugDataProvider] for lazy supply of the server list.
     */
    public constructor(preInstalledServers: DebugDataProvider<List<DebugServer>>) : this(
        preInstalledServers = preInstalledServers.provideData()
    )

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return ServersPluginContainer(preInstalledServers, commonContainer)
    }

    @Composable
    override fun content() {
        ServersScreen(isEditMode = false)
    }

    @Composable
    override fun settingsContent() {
        ServersScreen(isEditMode = true)
    }

    public companion object {
        internal const val NAME = "SERVERS"

        /**
         * Returns the currently selected server.
         *
         * Blocking call -- use only off the main thread
         * or in places where blocking is acceptable (e.g., an OkHttp interceptor).
         *
         * @throws IllegalArgumentException if [ServersPlugin] is not registered
         */
        public fun getSelectedServer(): DebugServer {
            return runBlocking {
                getPlugin<ServersPlugin>().getContainer<ServersPluginContainer>().serversRepository.getSelectedServer()
            }
        }

        /**
         * Returns the server marked as default.
         *
         * @throws IllegalArgumentException if [ServersPlugin] is not registered
         */
        public fun getDefaultServer(): DebugServer {
            return getPlugin<ServersPlugin>().getContainer<ServersPluginContainer>().serversRepository.getDefault()
        }
    }
}
