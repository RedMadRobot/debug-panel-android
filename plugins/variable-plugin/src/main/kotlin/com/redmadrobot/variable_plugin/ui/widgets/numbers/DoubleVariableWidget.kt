package com.redmadrobot.variable_plugin.ui.widgets.numbers

import android.text.method.DigitsKeyListener
import android.view.View

internal class DoubleVariableWidget : NumberVariableWidget<Double>(Double::class){

    override fun instantiateViewHolder(view: View): ViewHolder<Double> {
        return object : ViewHolder<Double>(view) {
            init {
                binding.inputLayout.editText?.filters = arrayOf(
                    DigitsKeyListener(null, true, true)
                )
            }

            override fun mapValue(text: String): Double {
                return if (text.any(Char::isDigit)) text.toDouble() else .0
            }
        }
    }

    override fun instantiateSettings(): NumberSettings<Double> {
        return NumberSettings(.0) { value, incrementStep -> value + incrementStep }
    }

    override fun instantiateSettingsViewHolder(
        view: View
    ): SettingsViewHolder<Double, NumberSettings<Double>> {
        return object : SettingsViewHolder<Double, NumberSettings<Double>>(view) {
            init {
                binding.variableNumberSettingsIncrementStep.filters = arrayOf(
                    DigitsKeyListener(null, true, true)
                )
            }

            override fun onIncrementalStepChanged(step: String): NumberSettings<Double> {
                return instantiateSettings().also { updatedSettings ->
                    updatedSettings.incrementStep =
                        if (step.any(Char::isDigit)) step.toDouble() else .0
                }
            }
        }
    }
}
