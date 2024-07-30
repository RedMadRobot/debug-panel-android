package com.redmadrobot.debug.core

import android.app.Activity
import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.redmadrobot.debug.core.internal.DebugEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object DebugPanel {
    fun initialize(
        application: Application,
        plugins: List<Any>,
        config: DebugPanelConfig = DebugPanelConfig.defaultConfig
    ) = Unit

    fun subscribeToEvents(lifecycleOwner: LifecycleOwner, onEvent: (DebugEvent) -> Unit) = Unit

    fun observeEvents(): Flow<DebugEvent> = emptyFlow()

    fun showPanel(fragmentManager: FragmentManager) = Unit

    fun showPanel(activity: Activity) = Unit
}
