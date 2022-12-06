package com.redmadrobot.debug.variable.ui.widgets

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.redmadrobot.debug.variable.plugin.VariableItem
import com.redmadrobot.debug.variable.plugin.VariableWidget
import com.redmadrobot.debug.variable.plugin.VariableWidgetViewHolder
import com.redmadrobot.variable_plugin.databinding.ItemVariableStringBinding

internal class StringVariableWidget : VariableWidget<String>(String::class) {

    override fun createViewHolder(parent: ViewGroup): VariableWidgetViewHolder<String> {
        val stringView = ItemVariableStringBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root

        return ViewHolder(stringView)
    }

    private class ViewHolder(
        itemView: View,
    ) : VariableWidgetViewHolder<String>(itemView) {

        private val binding = ItemVariableStringBinding.bind(itemView)

        private var valueTextWatcher: TextWatcher? = null

        override fun bind(
            item: VariableItem<String>,
            onValueChanged: (String) -> Unit
        ) = with(binding.inputLayout) {
            editText?.removeTextChangedListener(valueTextWatcher)

            hint = item.name
            editText?.setText(item.value)

            valueTextWatcher = editText?.addTextChangedListener { text ->
                onValueChanged.invoke(text?.toString().orEmpty())
            }
        }
    }
}
