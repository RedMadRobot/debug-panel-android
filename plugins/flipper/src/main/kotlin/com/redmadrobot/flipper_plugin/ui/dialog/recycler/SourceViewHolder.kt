package com.redmadrobot.flipper_plugin.ui.dialog.recycler

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.flipper_plugin.databinding.ItemFlipperSourceBinding
import com.redmadrobot.flipper_plugin.ui.dialog.data.SelectableSource

internal class SourceViewHolder(
    view: View,
    private val onSourceClick: (name: String) -> Unit,
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemFlipperSourceBinding.bind(itemView)

    fun bind(source: SelectableSource) {
        binding.sourceName.apply {
            text = source.name
            setOnClickListener {
                onSourceClick.invoke(source.name)
            }
        }

        binding.sourceSelected.isVisible = source.selected
    }
}
