package com.redmadrobot.feature_togles_plugin.data.repository

import android.content.Context

class PluginSettingsRepository(context: Context) {

    companion object {
        private const val NAME = ":feature-toggle"
        private const val OVERRIDE_FEATURE_TOGGLE_ENABLE = "OVERRIDE_FEATURE_TOGGLE_ENABLE"
    }

    private val sharedPreferences by lazy {
        val prefFileName = "${context.packageName}$NAME"
        context.getSharedPreferences(prefFileName, 0)
    }

    var overrideFeatureToggleEnable: Boolean
        get() = sharedPreferences.getBoolean(OVERRIDE_FEATURE_TOGGLE_ENABLE, true)
        set(value) {
            sharedPreferences.edit().putBoolean(OVERRIDE_FEATURE_TOGGLE_ENABLE, value).apply()
        }
}
