package com.redmadrobot.variable_plugin.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.redmadrobot.variable_plugin.databinding.ItemVariableBinding
import com.redmadrobot.variable_plugin.ui.model.VariableEvent
import com.redmadrobot.variable_plugin.ui.model.VariableItem
import com.redmadrobot.variable_plugin.ui.model.VariableSettings

internal class VariableAdapter(
    private val onEventAction: (event: VariableEvent) -> Unit,
    private val onItemSettingsRequested: (itemName: String) -> VariableSettings,
) : ListAdapter<VariableItem, VariableViewHolder>(VariableItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariableViewHolder {
        val view = ItemVariableBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .root

        return VariableViewHolder(view, onEventAction)
    }

    override fun onBindViewHolder(holder: VariableViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemSettingsRequested.invoke(item.name))
    }
}
