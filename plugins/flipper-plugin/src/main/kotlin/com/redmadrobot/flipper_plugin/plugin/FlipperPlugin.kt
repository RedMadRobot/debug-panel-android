package com.redmadrobot.flipper_plugin.plugin

import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer

public class FlipperPlugin(
) : Plugin() {

    internal companion object {
        const val NAME = "FLIPPER PLUGIN"
    }

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return FlipperPluginContainer()
    }
}
