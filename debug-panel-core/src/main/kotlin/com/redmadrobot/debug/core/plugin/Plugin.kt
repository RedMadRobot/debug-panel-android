package com.redmadrobot.debug.core.plugin

import androidx.fragment.app.Fragment
import com.redmadrobot.debug.core.CommonContainer
import com.redmadrobot.debug.core.DebugPanelInstance
import com.redmadrobot.debug.core.internal.DebugEvent

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

    public open fun getFragment(): Fragment? = null

    public open fun getSettingFragment(): Fragment? = null

    public abstract fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer

    public abstract fun getName(): String
}
