package com.redmadrobot.flipper_plugin.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.flipper_plugin.databinding.ItemFlipperFeatureBooleanBinding

internal class BooleanFeatureViewHolder(
    itemView: View,
    private val onCheckedStateChange: (name: CharSequence, checked: Boolean) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemFlipperFeatureBooleanBinding.bind(itemView)

    fun bind(name: CharSequence, checked: Boolean) {
        binding.checkbox.apply {
            text = name
            isChecked = checked

            setOnCheckedChangeListener { _, isChecked ->
                onCheckedStateChange.invoke(name, isChecked)
            }
        }
    }
}
