package com.redmadrobot.variable_plugin.ui.widgets.numbers

import android.text.method.DigitsKeyListener
import android.view.View

internal class IntVariableWidget : NumberVariableWidget<Int>(Int::class) {

    override fun instantiateViewHolder(view: View): ViewHolder<Int> {
        return object : ViewHolder<Int>(view) {
            init {
                binding.inputLayout.editText?.filters = arrayOf(
                    DigitsKeyListener(null, true, false)
                )
            }

            override fun mapValue(text: String): Int {
                return if (text.any(Char::isDigit)) text.toInt() else 0
            }
        }
    }

    override fun instantiateSettings(): NumberSettings<Int> {
        return NumberSettings(0) { value, incrementStep -> value + incrementStep }
    }

    override fun instantiateSettingsViewHolder(
        view: View
    ): SettingsViewHolder<Int, NumberSettings<Int>> {
        return object : SettingsViewHolder<Int, NumberSettings<Int>>(view) {
            init {
                binding.variableNumberSettingsIncrementStep.filters = arrayOf(
                    DigitsKeyListener(null, true, false)
                )
            }

            override fun onIncrementalStepChanged(step: String): NumberSettings<Int> {
                return instantiateSettings().also { updatedSettings ->
                    updatedSettings.incrementStep = if (step.any(Char::isDigit)) step.toInt() else 0
                }
            }
        }
    }
}
