package com.redmadrobot.debug_panel_core.internal

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object DebugPanel {
    fun initialize(application: Application, plugins: List<Any>) = Unit

    fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit) = Unit

    fun observeEvents(): Flow<DebugEvent>? = emptyFlow()

    fun showPanel(fragmentManager: FragmentManager) = Unit
}
