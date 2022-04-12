package com.redmadrobot.debug_panel_core.internal

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.redmadrobot.debug_panel_core.DebugPanelInstance
import com.redmadrobot.debug_panel_core.inapp.DebugBottomSheet
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.util.ApplicationLifecycleHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public object DebugPanel {

    private var instance: DebugPanelInstance? = null
    public val isInitialized: Boolean
        get() = instance != null

    public fun initialize(
        application: Application,
        plugins: List<Plugin>,
        config: DebugPanelConfig = DebugPanelConfig.defaultConfig
    ) {
        createDebugPanelInstance(application, plugins)
        ApplicationLifecycleHandler(application, config.shakerMode).start()
    }

    public fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit) {
        instance?.getEventLiveData()?.observe(lifecycleOwner, Observer { onEvent.invoke(it) })
    }

    public fun observeEvents(): Flow<DebugEvent> {
        return instance?.getEventFlow() ?: emptyFlow()
    }

    public fun showPanel(fragmentManager: FragmentManager) {
        if (isInitialized) {
            DebugBottomSheet.show(fragmentManager)
        }
    }

    private fun createDebugPanelInstance(application: Application, plugins: List<Plugin>) {
        instance = DebugPanelInstance(application, plugins)
    }


}
