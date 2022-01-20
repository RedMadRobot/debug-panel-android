package com.redmadrobot.variable_plugin.ui.recycler

import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.redmadrobot.variable_plugin.ui.model.VariableEvent
import com.redmadrobot.variable_plugin.ui.model.VariableItem
import com.redmadrobot.variable_plugin.ui.model.VariableSetting
import com.redmadrobot.variable_plugin.ui.model.VariableSettings

internal class NumberVariableViewHolder(
    itemView: View,
    onEventAction: (event: VariableEvent) -> Unit,
) : StringVariableViewHolder(
    itemView, onEventAction
) {

    override val settingsSupported: Boolean = true

    private var incrementStepTextWatcher: TextWatcher? = null

    override fun updateInput(item: VariableItem) {
        super.updateInput(item)

        binding.inputLayout.editText?.inputType = InputType.TYPE_CLASS_PHONE
    }

    override fun updateSettings(item: VariableItem, settings: VariableSettings) {
        super.updateSettings(item, settings)

        updateAutoincrement(item, settings)
    }

    private fun updateAutoincrement(
        item: VariableItem,
        settings: VariableSettings
    ) = with(binding) {
        incrementStep.removeTextChangedListener(incrementStepTextWatcher)
        checkboxAutoincrement.setOnCheckedChangeListener(null)

        incrementStep.setText(settings.autoincrement.step.toString())

        incrementStepTextWatcher = incrementStep.addTextChangedListener { text ->
            updateSettingAutoincrement(
                item.name,
                step = text?.toString()?.toDoubleOrNull() ?: 0.0
            )
        }

        checkboxAutoincrement.isChecked = settings.autoincrement.isEnabled
        checkboxAutoincrement.setOnCheckedChangeListener { _, isChecked ->
            updateSettingAutoincrement(item.name, isChecked)
        }
    }

    private fun updateSettingAutoincrement(
        variableName: String,
        enabled: Boolean = binding.checkboxAutoincrement.isChecked,
        step: Double = binding.incrementStep.text?.toString()?.toDoubleOrNull() ?: 0.0,
    ) {
        onEventAction.invoke(
            VariableEvent.SettingChanged(
                variableName,
                VariableSetting.Autoincrement(enabled, step)
            )
        )
    }
}
