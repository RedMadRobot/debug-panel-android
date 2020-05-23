package com.redmadrobot.debug_panel.internal.plugin

import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel.internal.CommonContainer

abstract class Plugin {
    private lateinit var pluginContainer: PluginDependencyContainer

    internal fun start(commonContainer: CommonContainer): Plugin {
        pluginContainer = getPluginContainer(commonContainer)
        return this
    }

    fun <T> getContainer() = pluginContainer as T

    open fun getFragment(): Fragment? = null

    open fun getSettingFragment(): Fragment? = null

    abstract fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer

    abstract fun getName(): String
}
