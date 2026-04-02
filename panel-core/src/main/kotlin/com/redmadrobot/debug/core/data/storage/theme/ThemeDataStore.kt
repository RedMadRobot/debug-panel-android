package com.redmadrobot.debug.core.data.storage.theme

import android.content.Context
import com.redmadrobot.debug.uikit.theme.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

internal class ThemeDataStore(private val context: Context) {
    private val dataStore by lazy { context.themeStorage }

    fun getSelectedTheme(): ThemeMode {
        return runBlocking { dataStore.data.first().themeMode }
    }

    fun observeThemeMode(): Flow<ThemeMode> {
        return dataStore.data.map { it.themeMode }
    }

    suspend fun saveThemeMode(mode: ThemeMode) {
        dataStore.updateData { data -> data.copy(themeMode = mode) }
    }
}
