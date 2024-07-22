package com.redmadrobot.debug.core.plugin

import com.redmadrobot.debug.core.internal.CommonContainer


internal class PluginManager(val plugins: List<Plugin>) {

    private val containers = mutableMapOf<String, Plugin>()

    @Suppress("NewApi")
    fun start(commonContainer: CommonContainer) {
        plugins.forEach { plugin ->
            containers[plugin::class.java.name] = plugin.start(commonContainer)
        }
    }

    fun findPluginByName(pluginName: String): Plugin {
        return containers[pluginName]
            ?: throw IllegalStateException("Plugin $pluginName must be initialized in Debug Panel")
    }
}
