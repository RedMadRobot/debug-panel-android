package com.redmadrobot.debug.plugin.variable.ui.widgets.numbers

import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.redmadrobot.debug.plugin.variable.VariableWidgetSettingsViewHolder
import com.redmadrobot.debug.plugin.variable.databinding.ItemVariableNumberSettingsBinding

internal open class NumbersSettingsViewHolder<T : Number, TSettings : NumberSettings<T>>(
    itemView: View,
) : VariableWidgetSettingsViewHolder<T, TSettings>(itemView) {

    protected val binding = ItemVariableNumberSettingsBinding.bind(itemView)

    private var incrementalStepTextWatcher: TextWatcher? = null

    override fun bind(
        settings: TSettings,
        onSettingsChanged: (TSettings) -> Unit,
    ) = with(binding.variableNumberSettingsIncrementStep) {
        removeTextChangedListener(incrementalStepTextWatcher)

        setText(settings.incrementStep.toString())

        incrementalStepTextWatcher = addTextChangedListener { text ->
            onSettingsChanged.invoke(
                settings.apply {
                    incrementStep = text?.toString().orEmpty().toDoubleOrNull() ?: .0
                }
            )
        }
    }
}
