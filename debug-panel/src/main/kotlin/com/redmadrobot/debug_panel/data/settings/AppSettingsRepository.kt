package com.redmadrobot.debug_panel.data.settings

import android.content.SharedPreferences
import io.reactivex.Single

interface AppSettingsRepository {
    fun getSettings(): Single<List<SharedPreferences>>
}
