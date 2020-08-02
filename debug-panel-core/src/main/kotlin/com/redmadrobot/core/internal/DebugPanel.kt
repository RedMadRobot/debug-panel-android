package com.redmadrobot.core.internal

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.redmadrobot.core.DebugPanelInstance
import com.redmadrobot.core.plugin.Plugin
import com.redmadrobot.core.util.ActivityLifecycleHandler
import timber.log.Timber

object DebugPanel {

    fun initialize(application: Application, plugins: List<Plugin>) {
        createDebugPanelInstance(application, plugins)
        ActivityLifecycleHandler(application).start()
        initTimber()
    }

    fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit) {

    }

    private fun createDebugPanelInstance(application: Application, plugins: List<Plugin>) {
        DebugPanelInstance(application, plugins)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
