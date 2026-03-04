package com.redmadrobot.debug.plugin.servers.data.storage

import android.content.Context
import android.content.SharedPreferences

internal object SharedPreferencesProvider {
    private const val NAME = ":servers"

    fun get(context: Context): SharedPreferences {
        val prefFileName = "${context.packageName}$NAME"
        return context.getSharedPreferences(prefFileName, 0)
    }
}
