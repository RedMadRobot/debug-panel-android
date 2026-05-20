package com.redmadrobot.debug.core

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.redmadrobot.debug.core.annotation.DebugPanelInternal
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.ui.debugpanel.DebugPanelActivity
import com.redmadrobot.debug.core.util.ApplicationLifecycleHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * Main entry point for working with the debug panel.
 *
 * Before use, [initialize] must be called in [Application.onCreate].
 *
 * Initialization example:
 * ```
 * DebugPanel.initialize(
 *     application = this,
 *     plugins = listOf(
 *         ServersPlugin(preInstalledServers),
 *         KonfeaturePlugin(interceptor, konfeature),
 *     )
 * )
 * ```
 *
 * @see Plugin
 */
@OptIn(DebugPanelInternal::class)
public object DebugPanel {
    @Volatile
    private var instance: DebugPanelInstance? = null

    /** Returns `true` if the panel has been initialized via [initialize]. */
    public val isInitialized: Boolean
        get() = instance != null

    /**
     * Initializes the debug panel with the provided set of plugins.
     *
     * Must be called **once** in [Application.onCreate].
     *
     * @param application the [Application] instance
     * @param plugins list of plugins to be displayed in the panel
     */
    public fun initialize(
        application: Application,
        plugins: List<Plugin>,
    ) {
        createDebugPanelInstance(application, plugins)
        ApplicationLifecycleHandler(application).start()
    }

    /**
     * Subscribes to plugin events with lifecycle binding.
     *
     * @param lifecycleOwner lifecycle owner (Activity, Fragment)
     * @param onEvent callback invoked when an event is received
     */
    public fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit) {
        instance?.getEventLiveData()?.observe(lifecycleOwner, Observer { onEvent.invoke(it) })
    }

    /**
     * Returns a [Flow] of events from plugins.
     *
     * If the panel is not initialized, returns an empty Flow.
     */
    public fun observeEvents(): Flow<DebugEvent> {
        return instance?.getEventFlow() ?: emptyFlow()
    }

    /**
     * Opens the Activity containing the debug panel.
     *
     * Does nothing if the panel has not been initialized.
     *
     * @param activity the Activity from which the panel will be launched
     */
    public fun showPanel(activity: Activity) {
        if (isInitialized) {
            openDebugPanel(activity)
        }
    }

    internal fun getInstance(): DebugPanelInstance? = instance

    private fun openDebugPanel(activity: Activity) {
        val intent = Intent(activity, DebugPanelActivity::class.java)
        activity.startActivity(intent)
    }

    private fun createDebugPanelInstance(application: Application, plugins: List<Plugin>) {
        instance = DebugPanelInstance(application, plugins)
    }
}
