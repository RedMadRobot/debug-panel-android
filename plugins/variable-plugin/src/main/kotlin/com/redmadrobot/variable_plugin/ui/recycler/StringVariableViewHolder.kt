package com.redmadrobot.variable_plugin.ui.recycler

import android.text.TextWatcher
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.variable_plugin.databinding.ItemVariableStringBinding
import com.redmadrobot.variable_plugin.ui.model.VariableEvent
import com.redmadrobot.variable_plugin.ui.model.VariableItem
import com.redmadrobot.variable_plugin.ui.model.VariableSetting
import com.redmadrobot.variable_plugin.ui.model.VariableSettings

internal open class StringVariableViewHolder(
    itemView: View,
    protected val onEventAction: (event: VariableEvent) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

    protected open val settingsSupported = false

    protected val binding = ItemVariableStringBinding.bind(itemView)

    private var valueTextWatcher: TextWatcher? = null

    fun bind(item: VariableItem, settings: VariableSettings) {
        updateInput(item)
        updateEnabler(item, settings)
        updateSettings(item, settings)
    }

    protected open fun updateInput(item: VariableItem) = with(binding.inputLayout) {
        editText?.removeTextChangedListener(valueTextWatcher)

        hint = item.name
        editText?.setText(item.value)

        valueTextWatcher = editText?.addTextChangedListener { text ->
            onEventAction.invoke(
                VariableEvent.ValueChange(item.name, text?.toString().orEmpty())
            )
        }
    }

    protected open fun updateSettings(
        item: VariableItem,
        settings: VariableSettings
    ) = with(binding) {
        settingsGroup.isGone = true

        buttonSettings.setOnClickListener(null)
        buttonSettings.isVisible = settingsSupported

        if (settingsSupported) {
            buttonSettings.setOnClickListener {
                settingsGroup.isVisible = settingsGroup.isGone
            }
        }
    }

    private fun updateEnabler(
        item: VariableItem,
        settings: VariableSettings
    ) = with(binding.checkboxEnabled) {
        setOnCheckedChangeListener(null)

        isChecked = settings.isEnabled

        setOnCheckedChangeListener { _, isChecked ->
            onEventAction.invoke(
                VariableEvent.SettingChanged(
                    item.name,
                    VariableSetting.Enabled(isChecked)
                )
            )
        }
    }
}
