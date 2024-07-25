package com.redmadrobot.debug.core.extension

import androidx.fragment.app.Fragment
import com.redmadrobot.debug.core.DebugPanelInstance
import com.redmadrobot.debug.core.annotation.DebugPanelInternal
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.ui.debugpanel.DebugActivity

@PublishedApi
internal fun getPlugin(pluginName: String): Plugin {
    val plugin = DebugPanelInstance.instance?.getPluginManger()?.findPluginByName(pluginName)
    return requireNotNull(plugin)
}

internal fun getAllPlugins(): List<Plugin> {
    return DebugPanelInstance.instance?.getPluginManger()?.plugins ?: emptyList()
}

@DebugPanelInternal
public inline fun <reified T : Plugin> getPlugin(): T {
    val plugin = getPlugin(T::class.java.name)
    return plugin as T
}

@DebugPanelInternal
public fun Fragment.isSettingMode(): Boolean {
    return activity?.javaClass == DebugActivity::class.java
}