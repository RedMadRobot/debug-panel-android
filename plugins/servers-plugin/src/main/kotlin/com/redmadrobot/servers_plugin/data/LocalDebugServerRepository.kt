package com.redmadrobot.servers_plugin.data

import android.content.Context
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.data.storage.DebugServersDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class LocalDebugServerRepository(
    private val context: Context,
    private val debugServersDao: DebugServersDao,
    private val preInstalledServers: List<DebugServer>
) : DebugServerRepository {

    companion object {
        private const val NAME = ":servers"
        private const val SELECTED_SERVER_URL = "SELECTED_SERVER_URL"
        private const val SELECTED_SERVER_NAME = "SELECTED_SERVER_NAME"
    }


    private val sharedPreferences by lazy {
        val prefFileName = "${context.packageName}$NAME"
        context.getSharedPreferences(prefFileName, 0)
    }


    override fun getPreInstalledServers(): List<DebugServer> {
        return preInstalledServers
    }

    override fun saveSelectedServer(selectedServer: DebugServer) {
        sharedPreferences.edit().apply {
            putString(SELECTED_SERVER_NAME, selectedServer.name)
            putString(SELECTED_SERVER_URL, selectedServer.url)
        }.apply()
    }

    override fun getSelectedServer(): DebugServer {
        val serverName = sharedPreferences.getString(SELECTED_SERVER_NAME, null)
        val serverUrl = sharedPreferences.getString(SELECTED_SERVER_URL, null)

        return if (serverName != null && serverUrl != null) {
            preInstalledServers.find {
                it.name == serverName && it.url == serverUrl
            } ?: debugServersDao.getServer(serverName, serverUrl) ?: getDefault()
        } else {
            getDefault()
        }
    }

    override fun getDefault(): DebugServer {
        return preInstalledServers.first { it.isDefault }
    }

    override suspend fun addServer(server: DebugServer) {
        withContext(Dispatchers.IO) {
            debugServersDao.insert(server)
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
