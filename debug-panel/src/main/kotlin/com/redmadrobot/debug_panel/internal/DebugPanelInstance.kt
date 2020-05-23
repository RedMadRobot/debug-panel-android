package com.redmadrobot.debug_panel.internal

import android.app.Application
import android.content.Context
import com.redmadrobot.debug_panel.internal.plugin.Plugin
import com.redmadrobot.debug_panel.internal.plugin.PluginManager
import com.redmadrobot.debug_panel.util.ActivityLifecycleHandler

internal class DebugPanelInstance constructor(
    application: Application,
    plugins: List<Plugin> = emptyList()
) {

    private var commonContainer: CommonContainer? = null
    private var pluginManager: PluginManager? = null


    init {
        ActivityLifecycleHandler(application).start()
        initContainer(application.applicationContext)
        initPluginManager(plugins, requireNotNull(commonContainer))
    }

    internal fun getContainer(): CommonContainer {
        return commonContainer
            ?: throw IllegalStateException("Container not initialised")
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
