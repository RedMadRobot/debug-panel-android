package com.redmadrobot.flipper_plugin.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.flipper_plugin.databinding.ItemFlipperFeatureGroupBinding

internal class GroupViewHolder(
    itemView: View,
    private val onTogglesStateChanged: (groupName: String, checked: Boolean) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemFlipperFeatureGroupBinding.bind(itemView)

    fun bind(name: String, allEnabled: Boolean) = with(binding) {
        everyToggleInGroup.setOnCheckedChangeListener(null)

        groupName.text = name
        everyToggleInGroup.isChecked = allEnabled

        everyToggleInGroup.setOnCheckedChangeListener { _, isChecked ->
            onTogglesStateChanged.invoke(name, isChecked)
        }
    }
}
