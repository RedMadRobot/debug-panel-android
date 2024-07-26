package com.redmadrobot.debug.core

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.redmadrobot.debug.core.annotation.DebugPanelInternal
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.ui.debugpanel.DebugBottomSheetActivity
import com.redmadrobot.debug.core.util.ApplicationLifecycleHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(DebugPanelInternal::class)
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

    public fun showPanel(activity: Activity) {
        if (isInitialized) {
            openDebugBottomSheet(activity)
        }
    }

    private fun openDebugBottomSheet(activity: Activity) {
        val intent = Intent(activity, DebugBottomSheetActivity::class.java)
        activity.startActivity(intent)
    }

    private fun createDebugPanelInstance(application: Application, plugins: List<Plugin>) {
        instance = DebugPanelInstance(application, plugins)
    }
}
