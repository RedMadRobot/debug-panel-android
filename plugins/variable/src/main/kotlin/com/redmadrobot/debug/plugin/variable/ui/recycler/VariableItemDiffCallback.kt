package com.redmadrobot.debug.plugin.variable.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.redmadrobot.debug.plugin.variable.VariableItem

internal class VariableItemDiffCallback : DiffUtil.ItemCallback<VariableItem<Any>>() {

    override fun areItemsTheSame(oldItem: VariableItem<Any>, newItem: VariableItem<Any>): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: VariableItem<Any>, newItem: VariableItem<Any>): Boolean {
        return oldItem == newItem
    }
}
