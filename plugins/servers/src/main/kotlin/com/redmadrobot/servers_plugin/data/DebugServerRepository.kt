package com.redmadrobot.servers_plugin.data

import com.redmadrobot.servers_plugin.data.model.DebugServer

internal interface DebugServerRepository {

    fun getPreInstalledServers(): List<DebugServer>

    fun saveSelectedServer(selectedServer: DebugServer)


    fun getDefault(): DebugServer

    suspend fun getSelectedServer(): DebugServer

    suspend fun getServers(): List<DebugServer>

    suspend fun addServer(server: DebugServer)

    suspend fun removeServer(server: DebugServer)

    suspend fun updateServer(server: DebugServer)
}
