package com.redmadrobot.debug_panel.data.settings

import android.content.SharedPreferences
import io.reactivex.Single

class AppSettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : AppSettingsRepository {

    override fun getSettings(): Single<Map<String, *>> {
        return Single.fromCallable {
            sharedPreferences.all
        }
    }
}
