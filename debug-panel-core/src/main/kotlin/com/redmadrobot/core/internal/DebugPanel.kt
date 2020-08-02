package com.redmadrobot.core.internal

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.redmadrobot.core.DebugPanelInstance
import com.redmadrobot.core.plugin.Plugin
import com.redmadrobot.core.util.ActivityLifecycleHandler
import timber.log.Timber

object DebugPanel {

    private var instance: DebugPanelInstance? = null

    fun initialize(application: Application, plugins: List<Plugin>) {
        createDebugPanelInstance(application, plugins)
        ActivityLifecycleHandler(application).start()
        initTimber()
    }

    fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit) {
        instance?.getEventLiveData()?.observe(lifecycleOwner, Observer { onEvent.invoke(it) })
    }

    private fun createDebugPanelInstance(application: Application, plugins: List<Plugin>) {
        instance = DebugPanelInstance(application, plugins)
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
