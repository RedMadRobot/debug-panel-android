package com.redmadrobot.variable_plugin.plugin

import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.variable_plugin.ui.VariableFragment

public class VariablePlugin : Plugin() {

    public companion object {
        internal const val NAME = "VARIABLE"
    }

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return VariablePluginContainer(
            container = commonContainer,
        )
    }

    override fun getFragment(): Fragment {
        return VariableFragment()
    }
}

private val variableRepository by lazy(LazyThreadSafetyMode.NONE) {
    getPlugin<VariablePlugin>()
        .getContainer<VariablePluginContainer>()
        .variableRepository
}

public fun <T> T.toDebugVariable(
    name: String,
): T {
    require(this is Number || this is String || this is Boolean)

    return variableRepository.getDebugVariableValue(
        name = name,
        defaultValue = this,
        variableClass = this!!::class,
    )
}
