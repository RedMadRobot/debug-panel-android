package com.redmadrobot.servers_plugin.data.repository

import android.content.Context
import com.redmadrobot.servers_plugin.data.model.DebugServer

class PluginSettingsRepository(context: Context) {
    companion object {
        private const val NAME = ":servers"
        private const val SELECTED_SERVER_URL = "SELECTED_SERVER_URL"
        private const val SELECTED_SERVER_NAME = "SELECTED_SERVER_NAME"
    }

    private val sharedPreferences by lazy {
        val prefFileName = "${context.packageName}$NAME"
        context.getSharedPreferences(prefFileName, 0)
    }

    fun saveSelectedServer(selectedServer: DebugServer) {
        sharedPreferences.edit().apply {
            putString(SELECTED_SERVER_NAME, selectedServer.name)
            putString(SELECTED_SERVER_URL, selectedServer.url)
        }.apply()
    }

    fun getSelectedServer(): DebugServer {
        val serverName = sharedPreferences.getString(SELECTED_SERVER_NAME, null)
        val serverUrl = sharedPreferences.getString(SELECTED_SERVER_URL, null)
        return if (serverName != null && serverUrl != null) {
            DebugServer(name = serverName, url = serverUrl)
        } else {
            DebugServer.getEmpty()
        }
    }
}
