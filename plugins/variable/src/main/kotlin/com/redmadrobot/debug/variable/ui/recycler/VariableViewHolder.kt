package com.redmadrobot.debug.variable.ui.recycler

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.debug.variable.plugin.VariableItem
import com.redmadrobot.debug.variable.plugin.VariableWidget
import com.redmadrobot.debug.variable.plugin.VariableWidgetSettings
import com.redmadrobot.debug.variable.ui.model.VariableEvent
import com.redmadrobot.variable_plugin.databinding.ItemVariableBinding

internal class VariableViewHolder<T : Any>(
    itemView: View,
    widget: VariableWidget<T>,
    private val onEventAction: (event: VariableEvent) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemVariableBinding.bind(itemView)
    private val widgetViewHolder = widget.createViewHolder(binding.root)
    private val widgetSettingsViewHolder = widget.createSettingsViewHolder(binding.root)

    init {
        binding.variableContentContainer.addView(widgetViewHolder.itemView)

        if (widgetSettingsViewHolder != null) {
            binding.variableButtonSettings.isVisible = true
            binding.variableButtonSettings.setOnClickListener {
                binding.variableSettingsContainer.apply { isVisible = isGone }
            }
            binding.variableSettingsContainer.addView(widgetSettingsViewHolder.itemView)
        }
    }

    fun bind(
        item: VariableItem<T>,
        settings: VariableWidgetSettings<T>?,
        enabled: Boolean,
    ) {
        widgetViewHolder.bind(item) { newValue ->
            onEventAction.invoke(
                VariableEvent.ValueChanged(
                    variableName = item.name,
                    newValue = newValue,
                )
            )
        }

        binding.variableCheckbox.apply {
            setOnCheckedChangeListener(null)

            isChecked = enabled

            setOnCheckedChangeListener { _, isChecked ->
                onEventAction.invoke(
                    VariableEvent.EnabledStatusChanged(
                        variableName = item.name,
                        enabled = isChecked,
                    )
                )
            }
        }

        if (settings != null && widgetSettingsViewHolder != null) {
            widgetSettingsViewHolder.bind(settings) { newSettings ->
                onEventAction.invoke(
                    VariableEvent.SettingsChanged(
                        variableName = item.name,
                        newSettings = newSettings,
                    )
                )
            }
        }
    }
}
