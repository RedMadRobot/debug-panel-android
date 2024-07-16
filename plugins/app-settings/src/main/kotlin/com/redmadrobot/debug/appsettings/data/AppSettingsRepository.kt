package com.redmadrobot.debug.appsettings.data

import android.content.SharedPreferences

internal interface AppSettingsRepository {
    fun getSettings(): List<SharedPreferences>

    fun updateSetting(key: String, value: Any)
}
