package com.redmadrobot.app_settings_plugin.data

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single

internal interface AppSettingsRepository {
    fun getSettings(): Single<List<SharedPreferences>>

    fun updateSetting(key: String, value: Any): Completable
}
