package com.redmadrobot.core

import android.content.Context
import com.redmadrobot.core.data.storage.AppDatabase
import com.redmadrobot.core.data.storage.PanelSettingsRepository

class CommonContainer(val context: Context) :
    PluginDependencyContainer {

    val dataBaseInstance by lazy { AppDatabase.getInstance(context) }

    val panelSettingsRepository by lazy {
        PanelSettingsRepository(
            context
        )
    }
}
