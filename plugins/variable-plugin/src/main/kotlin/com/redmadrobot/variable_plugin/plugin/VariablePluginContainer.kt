package com.redmadrobot.variable_plugin.plugin

import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.variable_plugin.data.VariableRepository
import com.redmadrobot.variable_plugin.ui.VariableViewModel
import com.redmadrobot.variable_plugin.ui.widgets.BooleanVariableWidget
import com.redmadrobot.variable_plugin.ui.widgets.StringVariableWidget
import com.redmadrobot.variable_plugin.ui.widgets.numbers.DoubleVariableWidget
import com.redmadrobot.variable_plugin.ui.widgets.numbers.FloatVariableWidget
import com.redmadrobot.variable_plugin.ui.widgets.numbers.IntVariableWidget
import com.redmadrobot.variable_plugin.ui.widgets.numbers.LongVariableWidget

internal class VariablePluginContainer(
    private val container: CommonContainer,
    private val customWidgets: List<VariableWidget<Any>>,
) : PluginDependencyContainer {

    val variableRepository by lazy(LazyThreadSafetyMode.NONE) {
        VariableRepository().apply {
            addWidget(IntVariableWidget())
            addWidget(LongVariableWidget())
            addWidget(FloatVariableWidget())
            addWidget(DoubleVariableWidget())

            addWidget(StringVariableWidget())
            addWidget(BooleanVariableWidget())

            customWidgets.forEach(::addWidget)
        }
    }

    internal fun createVariableViewModel(): VariableViewModel {
        return VariableViewModel(
            repository = variableRepository,
        )
    }
}
