package com.redmadrobot.flipper_plugin.ui.dialog.recycler

import androidx.recyclerview.widget.DiffUtil
import com.redmadrobot.flipper_plugin.ui.dialog.data.SelectableSource

internal class SourceDiffCallback : DiffUtil.ItemCallback<SelectableSource>() {
    override fun areItemsTheSame(oldItem: SelectableSource, newItem: SelectableSource): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: SelectableSource, newItem: SelectableSource): Boolean {
        return oldItem == newItem
    }
}
