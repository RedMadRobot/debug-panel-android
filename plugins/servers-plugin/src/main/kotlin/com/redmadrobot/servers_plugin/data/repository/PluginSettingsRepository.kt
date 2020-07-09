package com.redmadrobot.servers_plugin.data.repository

import android.content.Context

class PluginSettingsRepository(context: Context) {
    companion object {
        private const val NAME = ":servers"
        private const val SELECTED_SERVER_HOST = "SELECTED_SERVER_HOST"
    }

    private val sharedPreferences by lazy {
        val prefFileName = "${context.packageName}$NAME"
        context.getSharedPreferences(prefFileName, 0)
    }

    fun saveSelectedServerHost(selectedServerHost: String) {
        sharedPreferences.edit().putString(SELECTED_SERVER_HOST, selectedServerHost).apply()
    }

    fun getSelectedServerHost(): String? {
        return sharedPreferences.getString(SELECTED_SERVER_HOST, null)
    }
}
