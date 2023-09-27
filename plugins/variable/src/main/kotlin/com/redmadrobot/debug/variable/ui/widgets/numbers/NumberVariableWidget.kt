package com.redmadrobot.debug.variable.ui.widgets.numbers

import android.text.InputFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import com.redmadrobot.debug.variable.plugin.VariableWidget
import com.redmadrobot.debug.variable.plugin.VariableWidgetSettings
import com.redmadrobot.debug.variable.plugin.VariableWidgetSettingsViewHolder
import com.redmadrobot.debug.variable.plugin.VariableWidgetViewHolder
import com.redmadrobot.debug.variable.databinding.ItemVariableNumberBinding
import com.redmadrobot.debug.variable.databinding.ItemVariableNumberSettingsBinding
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
    ): VariableWidgetSettingsViewHolder<T, VariableWidgetSettings<T>>? {
        val view = ItemVariableNumberSettingsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root

        return NumbersSettingsViewHolder<T, NumberSettings<T>>(view)
                as VariableWidgetSettingsViewHolder<T, VariableWidgetSettings<T>>
    }
}
