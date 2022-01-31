package com.redmadrobot.variable_plugin.ui.widgets.numbers

import android.text.InputFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import com.redmadrobot.variable_plugin.databinding.ItemVariableNumberBinding
import com.redmadrobot.variable_plugin.databinding.ItemVariableNumberSettingsBinding
import com.redmadrobot.variable_plugin.plugin.VariableSettings
import com.redmadrobot.variable_plugin.plugin.VariableWidget
import com.redmadrobot.variable_plugin.plugin.VariableWidgetSettingsViewHolder
import com.redmadrobot.variable_plugin.plugin.VariableWidgetViewHolder
import kotlin.reflect.KClass

internal abstract class NumberVariableWidget<T : Number>(
    kClass: KClass<T>,
) : VariableWidget<T>(kClass) {

    protected open val inputFilters: Array<InputFilter> = emptyArray()

    override fun createViewHolder(parent: ViewGroup): VariableWidgetViewHolder<T> {
        val view = ItemVariableNumberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root

        return NumbersViewHolder(view, kClass)
    }

    override fun getSupportedSettings(): NumberSettings<T> = NumberSettings()

    override fun createSettingsViewHolder(
        parent: ViewGroup
    ): VariableWidgetSettingsViewHolder<T, VariableSettings<T>>? {
        val view = ItemVariableNumberSettingsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root

        return NumbersSettingsViewHolder<T, NumberSettings<T>>(view)
                as VariableWidgetSettingsViewHolder<T, VariableSettings<T>>
    }
}
