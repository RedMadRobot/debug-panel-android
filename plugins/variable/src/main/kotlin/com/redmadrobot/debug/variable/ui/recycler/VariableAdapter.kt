package com.redmadrobot.debug.variable.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.redmadrobot.debug.variable.plugin.VariableItem
import com.redmadrobot.debug.variable.plugin.VariableWidget
import com.redmadrobot.debug.variable.plugin.VariableWidgetSettings
import com.redmadrobot.debug.variable.ui.model.VariableEvent
import com.redmadrobot.debug.variable.databinding.ItemVariableBinding

internal class VariableAdapter(
    private val onEventAction: (event: VariableEvent) -> Unit,
    private val onWidgetRequested: (kClassHashCode: Int) -> VariableWidget<Any>,
    private val onItemSettingsRequested: (itemName: String) -> VariableWidgetSettings<Any>?,
    private val onItemEnabledStatusRequested: (itemName: String) -> Boolean,
) : ListAdapter<VariableItem<Any>, VariableViewHolder<Any>>(VariableItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).kClass.hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariableViewHolder<Any> {
        return VariableViewHolder(
            widget = onWidgetRequested.invoke(viewType),
            onEventAction = onEventAction,
            itemView = ItemVariableBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root,
        )
    }

    override fun onBindViewHolder(holder: VariableViewHolder<Any>, position: Int) {
        val item = getItem(position)

        holder.bind(
            item = item,
            settings = onItemSettingsRequested.invoke(item.name),
            enabled = onItemEnabledStatusRequested.invoke(item.name)
        )
    }
}
