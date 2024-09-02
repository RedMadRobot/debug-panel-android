package com.redmadrobot.debug.core.plugin

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.core.DebugPanelInstance
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer

public abstract class Plugin {

    private lateinit var pluginContainer: PluginDependencyContainer

    internal fun start(commonContainer: CommonContainer): Plugin {
        pluginContainer = getPluginContainer(commonContainer)
        return this
    }

    public fun pushEvent(debugEvent: DebugEvent) {
        DebugPanelInstance.instance?.pushEvent(debugEvent)
    }

    public fun <T> getContainer(): T = pluginContainer as T

    @Composable
    public open fun content() {
    }

    public abstract fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer

    public abstract fun getName(): String
}
