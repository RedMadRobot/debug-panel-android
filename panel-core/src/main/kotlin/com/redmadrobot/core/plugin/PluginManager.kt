package com.redmadrobot.core.plugin

import com.redmadrobot.core.CommonContainer


internal class PluginManager(private val plugins: List<Plugin>) {

    private val containers = mutableMapOf<String, Plugin>()

    fun start(commonContainer: CommonContainer) {
        plugins.forEach { plugin ->
            containers[plugin::class.java.name] = plugin.start(commonContainer)
        }
    }

    fun findPluginByName(pluginName: String): Plugin {
        return containers[pluginName]
            ?: throw IllegalArgumentException("Plugin with $pluginName name non exist")
    }
}
