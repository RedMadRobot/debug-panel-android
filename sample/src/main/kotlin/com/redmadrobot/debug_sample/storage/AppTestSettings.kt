package com.redmadrobot.debug_sample.storage

import android.content.Context

class AppTestSettings(context: Context) {

    companion object {
        private const val TEST_DATA_SHARED_PREFERENCES = "TEST_DATA_SHARED_PREFERENCES"
        private const val TEST_DATA_BOOLEAN = "TEST_DATA_BOOLEAN"
        private const val TEST_DATA_LONG = "TEST_DATA_LONG"
        private const val TEST_DATA_STRING = "TEST_DATA_STRING"
        private const val TEST_DATA_INTEGER = "TEST_DATA_INTEGER"
    }

    val testSharedPreferences by lazy {
        context.getSharedPreferences(TEST_DATA_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    init {
        testSharedPreferences.edit().apply() {
            putBoolean(TEST_DATA_BOOLEAN, true)
            putString(TEST_DATA_STRING, "Test String")
            putLong(TEST_DATA_LONG, 144342411)
            putInt(TEST_DATA_INTEGER, 12222)
        }
            .apply()
    }
}
