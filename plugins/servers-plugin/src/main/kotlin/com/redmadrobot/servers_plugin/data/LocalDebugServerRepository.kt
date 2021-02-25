package com.redmadrobot.servers_plugin.data

import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.data.storage.DebugServersDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class LocalDebugServerRepository(
    private val debugServersDao: DebugServersDao,
    private val preInstalledServers: List<DebugServer>
) : DebugServerRepository {

    override suspend fun addServer(server: DebugServer) {
        withContext(Dispatchers.IO) {
            debugServersDao.insert(server)
        }
    }

    override suspend fun getPreInstalledServers(): List<DebugServer> {
        return withContext(Dispatchers.IO) {
            listOf(DebugServer.getEmpty()).plus(preInstalledServers)
        }
    }

    override suspend fun getServers(): List<DebugServer> {
        return withContext(Dispatchers.IO) {
            debugServersDao.getAll()
        }
    }

    override suspend fun removeServer(server: DebugServer) {
        withContext(Dispatchers.IO) {
            debugServersDao.remove(server)
        }
    }

    override suspend fun updateServer(server: DebugServer) {
        withContext(Dispatchers.IO) {
            debugServersDao.update(server)
        }
    }
}
