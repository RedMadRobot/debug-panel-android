package com.redmadrobot.debug_panel_core.internal

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.redmadrobot.debug_panel_core.DebugPanelInstance
import com.redmadrobot.debug_panel_core.inapp.DebugBottomSheet
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.util.ApplicationLifecycleHandler

public object DebugPanel {

    private var instance: DebugPanelInstance? = null

    public fun initialize(application: Application, plugins: List<Plugin>) {
        createDebugPanelInstance(application, plugins)
        ApplicationLifecycleHandler(application).start()
    }

    public fun isInitialized(): Boolean = instance != null

    public fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit) {
        instance?.getEventLiveData()?.observe(lifecycleOwner, Observer { onEvent.invoke(it) })
    }

    public fun showPanel(fragmentManager: FragmentManager) {
        if (isInitialized()) {
            DebugBottomSheet.show(fragmentManager)
        }
    }

    private fun createDebugPanelInstance(application: Application, plugins: List<Plugin>) {
        instance = DebugPanelInstance(application, plugins)
    }
}
