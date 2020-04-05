package com.redmadrobot.debug_panel.data.storage

import android.content.Context

class PreferenceRepository(context: Context) {

    companion object {
        private const val DEBUG_PANEL_POSTFIX = ":debug_panel"
        private const val OVERRIDE_FEATURE_TOGGLE_ENABLE = "OVERRIDE_FEATURE_TOGGLE_ENABLE"
        private const val SELECTED_SERVER_HOST = "SELECTED_SERVER_HOST"
    }

    private val sharedPreferences by lazy {
        val prefFileName = "${context.packageName}${DEBUG_PANEL_POSTFIX}"
        context.getSharedPreferences(prefFileName, 0)
    }

    var overrideFeatureToggleEnable: Boolean
        get() = sharedPreferences.getBoolean(OVERRIDE_FEATURE_TOGGLE_ENABLE, true)
        set(value) {
            sharedPreferences.edit().putBoolean(OVERRIDE_FEATURE_TOGGLE_ENABLE, value).apply()
        }


    fun saveSelectedServerHost(selectedServerHost: String) {
        sharedPreferences.edit().putString(SELECTED_SERVER_HOST, selectedServerHost).apply()
    }

    fun getSelectedServerHost(): String? {
        return sharedPreferences.getString(SELECTED_SERVER_HOST, null)
    }

    fun add(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    fun remove(key: String) {
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }
}
