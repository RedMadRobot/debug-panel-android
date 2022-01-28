package com.redmadrobot.variable_plugin.ui.widgets.numbers

import android.text.method.DigitsKeyListener
import android.view.View

internal class LongVariableWidget : NumberVariableWidget<Long>(Long::class){

    override fun instantiateViewHolder(view: View): ViewHolder<Long> {
        return object : ViewHolder<Long>(view) {
            init {
                binding.inputLayout.editText?.filters = arrayOf(
                    DigitsKeyListener(null, true, false)
                )
            }

            override fun mapValue(text: String): Long {
                return if (text.any(Char::isDigit)) text.toLong() else 0
            }
        }
    }

    override fun instantiateSettings(): NumberSettings<Long> {
        return NumberSettings(0) { value, incrementStep -> value + incrementStep }
    }

    override fun instantiateSettingsViewHolder(
        view: View
    ): SettingsViewHolder<Long, NumberSettings<Long>> {
        return object : SettingsViewHolder<Long, NumberSettings<Long>>(view) {
            init {
                binding.variableNumberSettingsIncrementStep.filters = arrayOf(
                    DigitsKeyListener(null, true, false)
                )
            }

            override fun onIncrementalStepChanged(step: String): NumberSettings<Long> {
                return instantiateSettings().also { updatedSettings ->
                    updatedSettings.incrementStep = if (step.any(Char::isDigit)) step.toLong() else 0
                }
            }
        }
    }
}
