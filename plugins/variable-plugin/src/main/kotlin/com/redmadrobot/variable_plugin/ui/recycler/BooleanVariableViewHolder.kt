package com.redmadrobot.variable_plugin.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.variable_plugin.databinding.ItemVariableBooleanBinding
import com.redmadrobot.variable_plugin.ui.model.VariableEvent
import com.redmadrobot.variable_plugin.ui.model.VariableItem
import com.redmadrobot.variable_plugin.ui.model.VariableSetting
import com.redmadrobot.variable_plugin.ui.model.VariableSettings

internal class BooleanVariableViewHolder(
    itemView: View,
    private val onEventAction: (event: VariableEvent) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemVariableBooleanBinding.bind(itemView)

    fun bind(item: VariableItem, settings: VariableSettings) {
        require(item.clazz == Boolean::class)

        binding.root.apply {
            setOnCheckedChangeListener(null)

            text = item.name
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
}
