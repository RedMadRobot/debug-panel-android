package com.redmadrobot.debug.core

import android.app.Activity
import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.redmadrobot.debug.core.internal.DebugEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Suppress("UnusedParameter", "OptionalUnit")
public object DebugPanel {
    public fun initialize(application: Application, plugins: List<Any>): Unit = Unit

    public fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit): Unit = Unit

    public fun observeEvents(): Flow<DebugEvent> = emptyFlow()

    public fun showPanel(fragmentManager: FragmentManager): Unit = Unit

    public fun showPanel(activity: Activity): Unit = Unit
}
