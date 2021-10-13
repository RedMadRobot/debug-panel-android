package com.redmadrobot.flipper_plugin.ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.redmadrobot.flipper_plugin.ui.item.FlipperFeatureItem

internal class FlipperFeaturesDiffCallback : DiffUtil.ItemCallback<FlipperFeatureItem>() {
    override fun areItemsTheSame(
        oldItem: FlipperFeatureItem,
        newItem: FlipperFeatureItem,
    ): Boolean {
        return oldItem.featureId == newItem.featureId
    }

    override fun areContentsTheSame(
        oldItem: FlipperFeatureItem,
        newItem: FlipperFeatureItem,
    ): Boolean {
        return oldItem == newItem
    }
}
