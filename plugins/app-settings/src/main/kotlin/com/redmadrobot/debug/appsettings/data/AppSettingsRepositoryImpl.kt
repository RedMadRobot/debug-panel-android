package com.redmadrobot.debug.appsettings.data

import android.content.SharedPreferences
import androidx.core.content.edit

internal class AppSettingsRepositoryImpl(
    private val sharedPreferencesList: List<SharedPreferences>
) : AppSettingsRepository {

    override fun getSettings(): List<SharedPreferences> {
        return sharedPreferencesList
    }

    override fun updateSetting(key: String, value: Any) {
        val sharedPreferences = sharedPreferencesList.find { it.contains(key) }
        sharedPreferences?.edit {
            when (value) {
                is Boolean -> putBoolean(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
            }
        }
    }
}
