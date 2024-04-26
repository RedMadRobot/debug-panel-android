package com.redmadrobot.flipper_plugin.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.redmadrobot.flipper_plugin.ui.data.FlipperItem
import com.redmadrobot.flipper_plugin.ui.data.FlipperItem.Feature
import com.redmadrobot.flipper_plugin.ui.data.FlipperItem.Group

internal class FlipperFeaturesDiffCallback : DiffUtil.ItemCallback<FlipperItem>() {
    override fun areItemsTheSame(
        oldItem: FlipperItem,
        newItem: FlipperItem,
    ): Boolean {
        return when {
            oldItem is Feature && newItem is Feature -> oldItem.id == newItem.id
            oldItem is Group && newItem is Group -> oldItem.name == newItem.name
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: FlipperItem,
        newItem: FlipperItem,
    ): Boolean {
        return oldItem == newItem
    }
}
