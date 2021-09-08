package com.redmadrobot.debug_panel_core

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel_core.internal.DebugEvent
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginManager
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class DebugPanelInstance constructor(
    application: Application,
    plugins: List<Plugin> = emptyList()
) {

    companion object {
        var instance: DebugPanelInstance? = null
    }

    private var commonContainer: CommonContainer? = null
    private var pluginManager: PluginManager? = null
    private val eventLiveData: MutableLiveData<DebugEvent> = MutableLiveData()
    private val eventSharedFlow: MutableSharedFlow<DebugEvent> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    init {
        initContainer(application.applicationContext)
        initPluginManager(plugins, requireNotNull(commonContainer))
        instance = this
    }

    internal fun getEventLiveData(): LiveData<DebugEvent> {
        return eventLiveData
    }

    internal fun getEventFlow(): Flow<DebugEvent> {
        return eventSharedFlow
    }

    internal fun pushEvent(debugEvent: DebugEvent) {
        eventLiveData.value = debugEvent
        eventSharedFlow.tryEmit(debugEvent)
    }

    internal fun getPluginManger(): PluginManager {
        return pluginManager
            ?: throw IllegalStateException("PluginManager not initialised")
    }


    private fun initContainer(context: Context) {
        commonContainer = CommonContainer(context)
    }

    private fun initPluginManager(
        plugins: List<Plugin>,
        commonContainer: CommonContainer
    ) {
        pluginManager = PluginManager(plugins).apply {
            start(commonContainer)
        }
    }
}
