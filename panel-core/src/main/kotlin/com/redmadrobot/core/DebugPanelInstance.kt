package com.redmadrobot.core

import android.app.Application
import android.content.Context

class DebugPanelInstance constructor(
    application: Application,
    plugins: List<Plugin> = emptyList()
) {

    companion object {
        var instance: DebugPanelInstance? = null
            get() = field ?: throw IllegalStateException("Debug panel must be initialised")
    }

    private var commonContainer: CommonContainer? = null
    private var pluginManager: PluginManager? = null


    init {
//        ActivityLifecycleHandler(application).start()
        initContainer(application.applicationContext)
        initPluginManager(plugins, requireNotNull(commonContainer))
        instance = this
    }

    fun getContainer(): CommonContainer {
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
