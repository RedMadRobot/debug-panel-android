package com.redmadrobot.variable_plugin.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.redmadrobot.variable_plugin.ui.model.VariableItem

internal class VariableItemDiffCallback : DiffUtil.ItemCallback<VariableItem>() {

    override fun areItemsTheSame(oldItem: VariableItem, newItem: VariableItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: VariableItem, newItem: VariableItem): Boolean {
        return oldItem == newItem
    }
}
