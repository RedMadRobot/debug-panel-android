package com.redmadrobot.debug.plugin.variable

import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.variable.data.VariableRepository
import com.redmadrobot.debug.plugin.variable.ui.VariableViewModel
import com.redmadrobot.debug.plugin.variable.ui.widgets.BooleanVariableWidget
import com.redmadrobot.debug.plugin.variable.ui.widgets.StringVariableWidget
import com.redmadrobot.debug.plugin.variable.ui.widgets.numbers.DoubleVariableWidget
import com.redmadrobot.debug.plugin.variable.ui.widgets.numbers.FloatVariableWidget
import com.redmadrobot.debug.plugin.variable.ui.widgets.numbers.IntVariableWidget
import com.redmadrobot.debug.plugin.variable.ui.widgets.numbers.LongVariableWidget
import com.redmadrobot.debug.plugin.variable.ui.widgets.numbers.ShortVariableWidget

internal class VariablePluginContainer(
    private val container: CommonContainer,
    private val customWidgets: List<VariableWidget<Any>>,
) : PluginDependencyContainer {

    val variableRepository by lazy(LazyThreadSafetyMode.NONE) {
        VariableRepository().apply {
            addWidget(IntVariableWidget())
            addWidget(LongVariableWidget())
            addWidget(ShortVariableWidget())
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
