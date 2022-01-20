package com.redmadrobot.variable_plugin.plugin

import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.variable_plugin.data.VariableRepository
import com.redmadrobot.variable_plugin.ui.VariableViewModel

internal class VariablePluginContainer(
    private val container: CommonContainer,
) : PluginDependencyContainer {

    val variableRepository by lazy(LazyThreadSafetyMode.NONE) {
        VariableRepository()
    }

    internal fun createVariableViewModel(): VariableViewModel {
        return VariableViewModel(
            repository = variableRepository,
        )
    }
}
