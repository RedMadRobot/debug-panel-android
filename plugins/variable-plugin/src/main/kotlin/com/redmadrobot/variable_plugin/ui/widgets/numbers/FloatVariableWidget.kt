package com.redmadrobot.variable_plugin.ui.widgets.numbers

import android.text.method.DigitsKeyListener
import android.view.View

internal class FloatVariableWidget : NumberVariableWidget<Float>(Float::class) {

    override fun instantiateViewHolder(view: View): ViewHolder<Float> {
        return object : ViewHolder<Float>(view) {
            init {
                binding.inputLayout.editText?.filters = arrayOf(
                    DigitsKeyListener(null, true, true)
                )
            }

            override fun mapValue(text: String): Float {
                return if (text.any(Char::isDigit)) text.toFloat() else 0F
            }
        }
    }

    override fun instantiateSettings(): NumberSettings<Float> {
        return NumberSettings(.0F) { value, incrementStep -> value + incrementStep }
    }

    override fun instantiateSettingsViewHolder(
        view: View
    ): SettingsViewHolder<Float, NumberSettings<Float>> {
        return object : SettingsViewHolder<Float, NumberSettings<Float>>(view) {
            init {
                binding.variableNumberSettingsIncrementStep.filters = arrayOf(
                    DigitsKeyListener(null, true, true)
                )
            }

            override fun onIncrementalStepChanged(step: String): NumberSettings<Float> {
                return instantiateSettings().also { updatedSettings ->
                    updatedSettings.incrementStep =
                        if (step.any(Char::isDigit)) step.toFloat() else 0F
                }
            }
        }
    }
}
