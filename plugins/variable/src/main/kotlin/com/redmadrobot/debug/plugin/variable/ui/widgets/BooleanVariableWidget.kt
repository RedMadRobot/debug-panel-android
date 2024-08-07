package com.redmadrobot.debug.plugin.variable.ui.widgets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redmadrobot.debug.plugin.variable.VariableItem
import com.redmadrobot.debug.plugin.variable.VariableWidget
import com.redmadrobot.debug.plugin.variable.VariableWidgetViewHolder
import com.redmadrobot.debug.plugin.variable.databinding.ItemVariableBooleanBinding

internal class BooleanVariableWidget : VariableWidget<Boolean>(Boolean::class){

    override fun createViewHolder(parent: ViewGroup): VariableWidgetViewHolder<Boolean> {
        val view = ItemVariableBooleanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root

        return ViewHolder(view)
    }

    private class ViewHolder(itemView: View) : VariableWidgetViewHolder<Boolean>(itemView) {

        private val binding = ItemVariableBooleanBinding.bind(itemView)

        override fun bind(
            item: VariableItem<Boolean>,
            onValueChanged: (Boolean) -> Unit
        ) = with(binding.root) {
            setOnCheckedChangeListener(null)

            text = item.name
            isChecked = item.value

            setOnCheckedChangeListener { _, isChecked ->
                onValueChanged.invoke(isChecked)
            }
        }
    }
}
