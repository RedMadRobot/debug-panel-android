package com.redmadrobot.flipper_plugin.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.flipper_plugin.databinding.ItemFlipperFeatureGroupBinding

internal class GroupViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemFlipperFeatureGroupBinding.bind(itemView)

    fun bind(groupName: String) {
        binding.groupName.text = groupName
    }
}
