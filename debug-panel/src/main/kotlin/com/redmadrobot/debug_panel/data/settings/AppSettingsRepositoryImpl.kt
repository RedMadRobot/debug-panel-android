package com.redmadrobot.debug_panel.data.settings

import android.content.SharedPreferences
import com.redmadrobot.debug_panel.extension.subscribeOnIo
import io.reactivex.Single

class AppSettingsRepositoryImpl(
    private val sharedPreferences: List<SharedPreferences>
) : AppSettingsRepository {

    override fun getSettings(): Single<List<SharedPreferences>> {
        return Single.fromCallable { sharedPreferences }
            .subscribeOnIo()
    }
}
