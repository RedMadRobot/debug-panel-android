package com.redmadrobot.debug.core.data.storage.theme

import android.content.Context
import androidx.datastore.dataStore

internal val Context.themeStorage by dataStore(
    fileName = "debug_panel_theme.json",
    serializer = ThemeDataSerializer,
)
