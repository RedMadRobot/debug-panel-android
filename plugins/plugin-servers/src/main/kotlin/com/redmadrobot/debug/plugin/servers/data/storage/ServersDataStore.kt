package com.redmadrobot.debug.plugin.servers.data.storage

import android.content.Context
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import kotlinx.coroutines.flow.first

internal class ServersDataStore(private val context: Context) {
    private val dataStore by lazy { context.serversStorage }

    suspend fun getAll(): List<DebugServer> {
        return dataStore.data.first().servers
    }

    suspend fun add(server: DebugServer) {
        dataStore.updateData { data -> data.copy(servers = data.servers + server) }
    }

    suspend fun remove(server: DebugServer) {
        dataStore.updateData { data -> data.copy(servers = data.servers - server) }
    }

    suspend fun update(oldServer: DebugServer, newServer: DebugServer) {
        dataStore.updateData { data ->
            val updatedServers = data.servers.map { if (it == oldServer) newServer else it }
            data.copy(servers = updatedServers)
        }
    }

    suspend fun saveSelected(server: DebugServer) {
        dataStore.updateData { data -> data.copy(selectedServer = server) }
    }

    suspend fun getSelected(): DebugServer? {
        return dataStore.data.first().selectedServer
    }
}
