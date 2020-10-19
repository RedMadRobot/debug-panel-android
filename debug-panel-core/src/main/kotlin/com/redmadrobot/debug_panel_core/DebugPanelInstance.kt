package com.redmadrobot.debug_panel_core

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel_core.internal.DebugEvent
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginManager

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


    init {
        initContainer(application.applicationContext)
        initPluginManager(plugins, requireNotNull(commonContainer))
        instance = this
    }

    internal fun getEventLiveData(): LiveData<DebugEvent> {
        return eventLiveData
    }

    internal fun pushEvent(debugEvent: DebugEvent) {
        eventLiveData.value = debugEvent
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
