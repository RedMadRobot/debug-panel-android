package com.redmadrobot.debug_panel.internal

import android.app.Application
import com.redmadrobot.debug_panel.internal.plugin.Plugin
import timber.log.Timber

object DebugPanel {

    internal var instance: DebugPanelInstance? = null
        get() = field ?: throw IllegalStateException("Debug panel must be initialised")

    fun initialize(application: Application, plugins: List<Plugin>) {
        createDebugPanelInstance(application, plugins)
        initTimber()
    }

    private fun createDebugPanelInstance(
        application: Application,
        plugins: List<Plugin>
    ) {
        instance = DebugPanelInstance(application, plugins)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
