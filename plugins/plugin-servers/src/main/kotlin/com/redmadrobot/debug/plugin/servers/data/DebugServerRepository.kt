package com.redmadrobot.debug.plugin.servers.data

import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.storage.ServersDataStore

internal class DebugServerRepository(
    private val serversDataStore: ServersDataStore,
    private val preInstalledServers: List<DebugServer>
) {
    fun getPreInstalledServers(): List<DebugServer> = preInstalledServers

    fun getDefault(): DebugServer = preInstalledServers.first { it.isDefault }

    suspend fun saveSelectedServer(selectedServer: DebugServer) {
        serversDataStore.saveSelected(selectedServer)
    }

    suspend fun getSelectedServer(): DebugServer {
        return serversDataStore.getSelected() ?: getDefault()
    }

    suspend fun addServer(server: DebugServer) = serversDataStore.add(server)

    suspend fun getServers(): List<DebugServer> = serversDataStore.getAll()

    suspend fun removeServer(server: DebugServer) {
        if (server == getSelectedServer()) saveSelectedServer(getDefault())
        serversDataStore.remove(server)
    }

    suspend fun updateServer(oldServer: DebugServer, newServer: DebugServer) =
        serversDataStore.update(oldServer, newServer)
}
