package com.redmadrobot.debug_panel.data.settings

import io.reactivex.Single

interface AppSettingsRepository{
    fun getSettings(): Single<Map<String, *>>
}
