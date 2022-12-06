package com.redmadrobot.debug.flipper.ui.dialog.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.redmadrobot.flipper_plugin.databinding.ItemFlipperSourceBinding
import com.redmadrobot.debug.flipper.ui.dialog.data.SelectableSource

internal class SourceSelectionAdapter(
    private val onSourceClick: (name: String) -> Unit,
) : ListAdapter<SelectableSource, SourceViewHolder>(
    SourceDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder(
            ItemFlipperSourceBinding.inflate(LayoutInflater.from(parent.context)).root,
            onSourceClick::invoke,
        )
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
