package com.redmadrobot.core.extension

import androidx.fragment.app.Fragment
import com.redmadrobot.core.DebugPanelInstance
import com.redmadrobot.core.plugin.Plugin


fun getPlugin(pluginName: String): Plugin {
    val plugin = DebugPanelInstance.instance?.getPluginManger()?.findPluginByName(pluginName)
    return requireNotNull(plugin)
}

fun getAllPlugins(): List<Plugin> {
    return DebugPanelInstance.instance?.getPluginManger()?.plugins ?: emptyList()
}

inline fun <reified T : Plugin> Fragment.getPlugin(): T {
    val plugin = getPlugin(T::class.java.name)
    return plugin as T
}

