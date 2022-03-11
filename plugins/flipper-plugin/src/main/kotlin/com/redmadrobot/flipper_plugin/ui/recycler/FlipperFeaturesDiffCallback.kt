package com.redmadrobot.flipper_plugin.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.redmadrobot.flipper_plugin.ui.data.FlipperFeature
import com.redmadrobot.flipper_plugin.ui.data.FlipperFeature.Group
import com.redmadrobot.flipper_plugin.ui.data.FlipperFeature.Item

internal class FlipperFeaturesDiffCallback : DiffUtil.ItemCallback<FlipperFeature>() {
    override fun areItemsTheSame(
        oldItem: FlipperFeature,
        newItem: FlipperFeature,
    ): Boolean {
        return when {
            oldItem is Item && newItem is Item -> oldItem.id == newItem.id
            oldItem is Group && newItem is Group -> oldItem.name == newItem.name
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: FlipperFeature,
        newItem: FlipperFeature,
    ): Boolean {
        return oldItem == newItem
    }
}
