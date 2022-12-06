package com.redmadrobot.debug.variable.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.redmadrobot.debug.variable.plugin.VariableItem

internal class VariableItemDiffCallback : DiffUtil.ItemCallback<VariableItem<Any>>() {

    override fun areItemsTheSame(oldItem: VariableItem<Any>, newItem: VariableItem<Any>): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: VariableItem<Any>, newItem: VariableItem<Any>): Boolean {
        return oldItem == newItem
    }
}
