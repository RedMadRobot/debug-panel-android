package com.redmadrobot.debug_panel.data.storage

import android.content.Context

class LocalStorage(context: Context) {

    companion object {
        private const val DEBUG_PANEL_POSTFIX = ":debug_panel"
    }

    private val sharedPreferences by lazy {
        val prefFileName = "${context.packageName}${DEBUG_PANEL_POSTFIX}"
        context.getSharedPreferences(prefFileName, 0)
    }

    fun add(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    fun remove(key: String) {
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }
}
