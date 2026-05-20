package com.redmadrobot.debug.core.extension

import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.core.annotation.DebugPanelInternal
import com.redmadrobot.debug.core.plugin.Plugin

@PublishedApi
internal fun getPlugin(pluginName: String): Plugin {
    val plugin = DebugPanel.getInstance()?.getPluginManager()?.findPluginByName(pluginName)
    return requireNotNull(plugin)
}

internal fun getAllPlugins(): List<Plugin> {
    return DebugPanel.getInstance()?.getPluginManager()?.plugins ?: emptyList()
}

/**
 * Returns the registered plugin of the specified type.
 *
 * @throws IllegalArgumentException if the plugin is not found in the registry
 * @see DebugPanel
 */
@DebugPanelInternal
public inline fun <reified T : Plugin> getPlugin(): T {
    val plugin = getPlugin(T::class.java.name)
    return plugin as T
}
