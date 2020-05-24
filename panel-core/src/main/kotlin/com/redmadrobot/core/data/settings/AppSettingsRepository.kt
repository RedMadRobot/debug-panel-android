package com.redmadrobot.core.data.settings

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single

interface AppSettingsRepository {
    fun getSettings(): Single<List<SharedPreferences>>

    fun updateSetting(key: String, value: Any): Completable
}
