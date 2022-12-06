package com.redmadrobot.debug.appsettings.data

import android.content.SharedPreferences

internal class DefaultSharedPreferences : SharedPreferences {
    override fun contains(key: String?): Boolean {
        return false
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return false
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return 0
    }

    override fun getAll(): MutableMap<String, *> {
        return emptyMap<String, Any>().toMutableMap()
    }

    override fun edit(): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun getLong(key: String?, defValue: Long): Long {
        TODO("Not yet implemented")
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        TODO("Not yet implemented")
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        TODO("Not yet implemented")
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun getString(key: String?, defValue: String?): String? {
        TODO("Not yet implemented")
    }
}
