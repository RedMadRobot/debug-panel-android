package com.redmadrobot.debug.core.plugin

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
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

    @Deprecated(
        "You shouldn't use fragments for you plugins. Please use Jetpack Compose",
        ReplaceWith("content()", "com.redmadrobot.debug.core.plugin.Plugin")
    )
    public open fun getFragment(): Fragment? = null

    @Deprecated(
        "You shouldn't use fragments for you plugins. Please use Jetpack Compose",
        ReplaceWith("content()", "com.redmadrobot.debug.core.plugin.Plugin")
    )
    public open fun getSettingFragment(): Fragment? = null

    @Composable
    public open fun content() {
    }

    @Composable
    public open fun settingsContent() {
    }

    public abstract fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer

    public abstract fun getName(): String
}
