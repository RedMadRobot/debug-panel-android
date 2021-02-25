package com.redmadrobot.servers_plugin.data

import com.redmadrobot.servers_plugin.data.model.DebugServer

internal interface DebugServerRepository {
    suspend fun addServer(server: DebugServer)

    suspend fun getPreInstalledServers(): List<DebugServer>

    suspend fun getServers(): List<DebugServer>

    suspend fun removeServer(server: DebugServer)

    suspend fun updateServer(server: DebugServer)
}
