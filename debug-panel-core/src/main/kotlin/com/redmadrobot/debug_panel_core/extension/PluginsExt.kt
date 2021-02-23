package com.redmadrobot.debug_panel_core.extension

import com.redmadrobot.debug_panel_core.DebugPanelInstance
import com.redmadrobot.debug_panel_core.plugin.Plugin


public fun getPlugin(pluginName: String): Plugin {
    val plugin = DebugPanelInstance.instance?.getPluginManger()?.findPluginByName(pluginName)
    return requireNotNull(plugin)
}

internal fun getAllPlugins(): List<Plugin> {
    return DebugPanelInstance.instance?.getPluginManger()?.plugins ?: emptyList()
}

public inline fun <reified T : Plugin> getPlugin(): T {
    val plugin = getPlugin(T::class.java.name)
    return plugin as T
}

