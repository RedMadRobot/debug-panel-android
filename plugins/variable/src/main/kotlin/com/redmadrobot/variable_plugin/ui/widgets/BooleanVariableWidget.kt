package com.redmadrobot.variable_plugin.ui.widgets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redmadrobot.variable_plugin.databinding.ItemVariableBooleanBinding
import com.redmadrobot.variable_plugin.plugin.VariableItem
import com.redmadrobot.variable_plugin.plugin.VariableWidget
import com.redmadrobot.variable_plugin.plugin.VariableWidgetViewHolder

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
