package com.redmadrobot.debug_panel.extension

import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.internal.plugin.Plugin

fun getPlugin(pluginName: String): Plugin {
    val plugin = DebugPanel.instance?.getPluginManger()?.findPluginByName(pluginName)
    return requireNotNull(plugin)
}

inline fun <reified T : Plugin> getPlugin(): T {
    val plugin = getPlugin(T::class.java.name)
    return plugin as T
}

