package com.redmadrobot.debug.plugin.servers.data

import android.content.Context
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.storage.DebugServersDao
import com.redmadrobot.debug.plugin.servers.data.storage.SharedPreferencesProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class DebugServerRepository(
    private val context: Context,
    private val debugServersDao: DebugServersDao,
    private val preInstalledServers: List<DebugServer>
) {

    companion object {
        private const val SELECTED_SERVER_URL = "SELECTED_SERVER_URL"
        private const val SELECTED_SERVER_NAME = "SELECTED_SERVER_NAME"
    }

    private val sharedPreferences by lazy {
        SharedPreferencesProvider.get(context)
    }

    fun getPreInstalledServers(): List<DebugServer> {
        return preInstalledServers
    }

    fun saveSelectedServer(selectedServer: DebugServer) {
        sharedPreferences.edit().apply {
            putString(SELECTED_SERVER_NAME, selectedServer.name)
            putString(SELECTED_SERVER_URL, selectedServer.url)
        }.apply()
    }

    suspend fun getSelectedServer(): DebugServer {
        val serverName = sharedPreferences.getString(SELECTED_SERVER_NAME, null)
        val serverUrl = sharedPreferences.getString(SELECTED_SERVER_URL, null)

        return if (serverName != null && serverUrl != null) {
            preInstalledServers.find { it.name == serverName && it.url == serverUrl }
                ?: debugServersDao.getServer(serverName, serverUrl)
                ?: getDefault()
        } else {
            getDefault()
        }
    }

    fun getDefault(): DebugServer {
        return preInstalledServers.first { it.isDefault }
    }

    suspend fun addServer(server: DebugServer) {
        withContext(Dispatchers.IO) {
            debugServersDao.insert(server)
        }
    }

    suspend fun getServers(): List<DebugServer> {
        return withContext(Dispatchers.IO) {
            debugServersDao.getAll()
        }
    }

    suspend fun removeServer(server: DebugServer) {
        withContext(Dispatchers.IO) {
            debugServersDao.remove(server)
        }
    }

    suspend fun updateServer(server: DebugServer) {
        withContext(Dispatchers.IO) {
            debugServersDao.update(server)
        }
    }
}
