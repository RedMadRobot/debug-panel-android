package com.redmadrobot.debug.core.internal

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.redmadrobot.debug.core.DebugPanelInstance
import com.redmadrobot.debug.core.inapp.DebugBottomSheet
import com.redmadrobot.debug.core.inapp.compose.DebugBottomSheet
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.util.ApplicationLifecycleHandler
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

    public fun showPanel(activity: Activity) {
        if (isInitialized) {
            val contentView = activity.window.decorView
                .findViewById<ViewGroup>(android.R.id.content) as FrameLayout

            val debugPanelComposeView = ComposeView(contentView.context).apply {
                setContent {
                    DebugBottomSheet(onClose = { contentView.removeView(this) })
                }
            }
            contentView.addView(debugPanelComposeView)
        }
    }

    private fun createDebugPanelInstance(application: Application, plugins: List<Plugin>) {
        instance = DebugPanelInstance(application, plugins)
    }
}
