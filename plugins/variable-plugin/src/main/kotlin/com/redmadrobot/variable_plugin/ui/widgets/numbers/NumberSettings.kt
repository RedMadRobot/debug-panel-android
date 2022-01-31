package com.redmadrobot.variable_plugin.ui.widgets.numbers

import com.redmadrobot.variable_plugin.plugin.VariableItem
import com.redmadrobot.variable_plugin.plugin.VariableSettings

internal open class NumberSettings<T : Number>(
    var incrementStep: Double = .0,
) : VariableSettings<T>() {

    override fun apply(item: VariableItem<T>): VariableItem<T> {
        val value = item.value
        val incrementResult = when (item.kClass) {
            Int::class -> value as Int + incrementStep.toInt()
            Long::class -> value as Long + incrementStep.toLong()
            Short::class -> value as Short + incrementStep.toInt().toShort()
            Float::class -> value as Float + incrementStep.toFloat()
            Double::class -> value as Double + incrementStep

            else -> {
                throw IllegalStateException(
                    "Can't apply ${NumberSettings::class.simpleName} to ${item.kClass}"
                )
            }
        }
        return item.copy(value = incrementResult as T)
    }
}
