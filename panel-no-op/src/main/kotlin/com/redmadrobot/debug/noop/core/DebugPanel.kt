package com.redmadrobot.debug.core

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * No-op implementation of [DebugPanel] for release builds.
 *
 * All methods are empty stubs and perform no actions.
 */
@Suppress("UnusedParameter", "OptionalUnit")
public object DebugPanel {
    /** Always `false`: there is no panel to initialize in release builds. */
    public val isInitialized: Boolean get() = false

    public fun initialize(application: Application, plugins: List<Any>): Unit = Unit

    public fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit): Unit = Unit

    public fun observeEvents(): Flow<DebugEvent> = emptyFlow()

    public fun showPanel(activity: Activity): Unit = Unit
}
