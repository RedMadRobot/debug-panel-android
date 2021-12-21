package com.redmadrobot.variable_plugin.ui.recycler

import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.variable_plugin.databinding.ItemVariableBinding
import com.redmadrobot.variable_plugin.ui.model.VariableEvent
import com.redmadrobot.variable_plugin.ui.model.VariableItem
import com.redmadrobot.variable_plugin.ui.model.VariableSetting
import com.redmadrobot.variable_plugin.ui.model.VariableSettings

internal class VariableViewHolder(
    itemView: View,
    private val onEventAction: (event: VariableEvent) -> Unit,
) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemVariableBinding.bind(itemView)

    private var valueTextWatcher: TextWatcher? = null
    private var incrementStepTextWatcher: TextWatcher? = null

    fun bind(item: VariableItem, settings: VariableSettings) {
        updateInput(item)
        updateEnabler(item, settings)
        updateSettings(item, settings)
    }

    private fun updateInput(item: VariableItem) {
        binding.inputLayout.editText?.removeTextChangedListener(valueTextWatcher)

        val variableIsString = item.className.contains(String::class.java.simpleName, true)
        val variableIsBoolean = item.className.contains(Boolean::class.java.simpleName, true)

        binding.inputLayout.hint = item.name
        binding.inputLayout.isEnabled = !variableIsBoolean
        binding.inputLayout.editText?.setText(item.value)
        binding.inputLayout.editText?.inputType = if (variableIsString) {
            InputType.TYPE_CLASS_TEXT
        } else {
            InputType.TYPE_CLASS_PHONE
        }

        valueTextWatcher = binding.inputLayout.editText?.addTextChangedListener { text ->
            onEventAction.invoke(
                VariableEvent.ValueChange(item.name, text?.toString().orEmpty())
            )
        }
    }

    private fun updateEnabler(item: VariableItem, settings: VariableSettings) {
        binding.checkboxEnabled.setOnCheckedChangeListener(null)

        binding.checkboxEnabled.isChecked = settings.isEnabled
        binding.checkboxEnabled.setOnCheckedChangeListener { _, isChecked ->
            onEventAction.invoke(
                VariableEvent.SettingChanged(
                    item.name,
                    VariableSetting.Enabled(isChecked)
                )
            )
        }
    }

    private fun updateSettings(item: VariableItem, settings: VariableSettings) {
        binding.numberSettingsGroup.isGone = true

        binding.buttonSettings.setOnClickListener(null)

        val shouldShowSettings =
            !item.className.contains(String::class.java.simpleName, true) &&
                    !item.className.contains(Boolean::class.java.simpleName, true)

        binding.buttonSettings.isVisible = shouldShowSettings

        if (shouldShowSettings) {
            binding.buttonSettings.setOnClickListener {
                binding.numberSettingsGroup.isVisible = binding.numberSettingsGroup.isGone
            }
        }

        updateAutoincrement(item, settings)
    }

    private fun updateAutoincrement(item: VariableItem, settings: VariableSettings) {
        binding.incrementStep.removeTextChangedListener(incrementStepTextWatcher)
        binding.checkboxAutoincrement.setOnCheckedChangeListener(null)

        binding.incrementStep.setText(settings.autoincrement.step.toString())
        incrementStepTextWatcher = binding.incrementStep.addTextChangedListener { text ->
            updateSettingAutoincrement(
                item.name,
                step = text?.toString()?.toDoubleOrNull() ?: 0.0
            )
        }

        binding.checkboxAutoincrement.isChecked = settings.autoincrement.isEnabled
        binding.checkboxAutoincrement.setOnCheckedChangeListener { _, isChecked ->
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
