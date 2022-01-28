package com.redmadrobot.variable_plugin.ui.widgets.numbers

import com.redmadrobot.variable_plugin.plugin.VariableItem
import com.redmadrobot.variable_plugin.plugin.VariableSettings

internal open class NumberSettings<T : Number>(
    var incrementStep: T,
    private val autoIncrement: (value: T, step: T) -> T
) : VariableSettings<T>() {

    final override fun apply(item: VariableItem<T>): VariableItem<T> {
        return item.copy(
            value = autoIncrement.invoke(item.value, incrementStep),
        )
    }
}
