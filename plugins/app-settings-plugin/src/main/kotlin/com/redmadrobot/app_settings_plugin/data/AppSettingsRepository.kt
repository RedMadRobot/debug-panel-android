package com.redmadrobot.app_settings_plugin.data

import android.content.SharedPreferences

internal interface AppSettingsRepository {
    fun getSettings(): List<SharedPreferences>

    fun updateSetting(key: String, value: Any)
}
