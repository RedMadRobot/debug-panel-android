package com.redmadrobot.debug_panel_core.internal

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

object DebugPanel {
    fun initialize(application: Application, plugins: List<Any>) = Unit

    fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit) = Unit

    fun showPanel(fragmentManager: FragmentManager) = Unit
}
