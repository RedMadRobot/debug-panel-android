package com.redmadrobot.debug_panel.internal

import android.content.Context
import com.redmadrobot.debug_panel.data.storage.AppDatabase
import com.redmadrobot.debug_panel.data.storage.PanelSettingsRepository
import com.redmadrobot.debug_panel.internal.plugin.PluginDependencyContainer

class CommonContainer(val context: Context) : PluginDependencyContainer {

    internal val dataBaseInstance by lazy { AppDatabase.getInstance(context) }

    internal val panelSettingsRepository by lazy { PanelSettingsRepository(context) }
}
