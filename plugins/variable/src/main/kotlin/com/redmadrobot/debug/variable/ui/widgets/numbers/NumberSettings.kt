package com.redmadrobot.debug.variable.ui.widgets.numbers

import com.redmadrobot.debug.variable.plugin.VariableItem
import com.redmadrobot.debug.variable.plugin.VariableWidgetSettings

internal open class NumberSettings<T : Number>(
    var incrementStep: Double = .0,
) : VariableWidgetSettings<T>() {

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
