package com.redmadrobot.debug_panel.internal

import android.app.Application
import com.redmadrobot.core.DebugPanelInstance
import com.redmadrobot.core.plugin.Plugin
import com.redmadrobot.debug_panel.util.ActivityLifecycleHandler
import timber.log.Timber

object DebugPanel {

    fun initialize(application: Application, plugins: List<Plugin>) {
        createDebugPanelInstance(application, plugins)
        ActivityLifecycleHandler(application).start()
        initTimber()
    }

    private fun createDebugPanelInstance(application: Application, plugins: List<Plugin>) {
        DebugPanelInstance(application, plugins)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
