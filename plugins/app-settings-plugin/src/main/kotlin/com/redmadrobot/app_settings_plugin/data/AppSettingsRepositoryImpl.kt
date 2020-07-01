package com.redmadrobot.app_settings_plugin.data

import android.content.SharedPreferences
import com.redmadrobot.core.extension.subscribeOnIo
import io.reactivex.Completable
import io.reactivex.Single

class AppSettingsRepositoryImpl(
    private val sharedPreferencesList: List<SharedPreferences>
) : AppSettingsRepository {

    override fun getSettings(): Single<List<SharedPreferences>> {
        return Single.fromCallable { sharedPreferencesList }
            .subscribeOnIo()
    }

    override fun updateSetting(key: String, value: Any): Completable {
        return Completable.fromCallable {
            val sharedPreferences = sharedPreferencesList.find { it.contains(key) }
            sharedPreferences?.edit()?.apply {
                when (value) {
                    is Boolean -> putBoolean(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is String -> putString(key, value)
                }
            }?.apply()
        }.subscribeOnIo()
    }
}
