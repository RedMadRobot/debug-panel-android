package com.redmadrobot.variable_plugin.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.variable_plugin.databinding.ItemVariableBooleanBinding
import com.redmadrobot.variable_plugin.databinding.ItemVariableStringBinding
import com.redmadrobot.variable_plugin.ui.model.VariableEvent
import com.redmadrobot.variable_plugin.ui.model.VariableItem
import com.redmadrobot.variable_plugin.ui.model.VariableSettings

internal class VariableAdapter(
    private val onEventAction: (event: VariableEvent) -> Unit,
    private val onItemSettingsRequested: (itemName: String) -> VariableSettings,
) : ListAdapter<VariableItem, RecyclerView.ViewHolder>(VariableItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when (item.clazz) {
            Int::class,
            Long::class,
            Short::class,
            Float::class,
            Double::class -> VariableType.NUMBER

            String::class -> VariableType.STRING
            Boolean::class -> VariableType.BOOLEAN

            else -> throw IllegalStateException("Not supported class: ${item.clazz}")
        }.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (VariableType.values()[viewType]) {
            VariableType.NUMBER -> parent.createViewHolderNumber()
            VariableType.STRING -> parent.createViewHolderString()
            VariableType.BOOLEAN -> parent.createViewHolderBoolean()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val settings = onItemSettingsRequested.invoke(item.name)

        when (holder) {
            is NumberVariableViewHolder -> holder.bind(item, settings)
            is StringVariableViewHolder -> holder.bind(item, settings)
            is BooleanVariableViewHolder -> holder.bind(item, settings)

            else -> throw IllegalStateException("Don't have holder for ${item.clazz}")
        }
    }

    private fun ViewGroup.createViewHolderNumber(): NumberVariableViewHolder {
        return NumberVariableViewHolder(
            ItemVariableStringBinding.inflate(
                LayoutInflater.from(context),
                this,
                false
            ).root,
            onEventAction,
        )
    }

    private fun ViewGroup.createViewHolderString(): StringVariableViewHolder {
        return StringVariableViewHolder(
            ItemVariableStringBinding.inflate(
                LayoutInflater.from(context),
                this,
                false
            ).root,
            onEventAction,
        )
    }

    private fun ViewGroup.createViewHolderBoolean(): BooleanVariableViewHolder {
        return BooleanVariableViewHolder(
            ItemVariableBooleanBinding.inflate(
                LayoutInflater.from(context),
                this,
                false
            ).root,
            onEventAction,
        )
    }

    private enum class VariableType {
        NUMBER,
        STRING,
        BOOLEAN,
    }
}
