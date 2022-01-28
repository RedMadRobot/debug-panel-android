package com.redmadrobot.variable_plugin.ui.widgets.numbers

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.redmadrobot.variable_plugin.databinding.ItemVariableNumberBinding
import com.redmadrobot.variable_plugin.databinding.ItemVariableNumberSettingsBinding
import com.redmadrobot.variable_plugin.databinding.ItemVariableStringBinding
import com.redmadrobot.variable_plugin.plugin.*
import kotlin.reflect.KClass

internal abstract class NumberVariableWidget<T : Number>(
    kClass: KClass<T>,
) : VariableWidget<T>(kClass) {

    override fun createViewHolder(parent: ViewGroup): VariableWidgetViewHolder<T> {
        val view = ItemVariableNumberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root

        return instantiateViewHolder(view)
    }

    override fun getSupportedSettings(): NumberSettings<T> {
        return instantiateSettings()
    }

    override fun createSettingsViewHolder(
        parent: ViewGroup
    ): VariableWidgetSettingsViewHolder<T, VariableSettings<T>>? {
        val view = ItemVariableNumberSettingsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root

        return instantiateSettingsViewHolder(view) as VariableWidgetSettingsViewHolder<T, VariableSettings<T>>
    }

    protected abstract fun instantiateViewHolder(view: View): ViewHolder<T>

    protected abstract fun instantiateSettings(): NumberSettings<T>

    protected abstract fun instantiateSettingsViewHolder(
        view: View
    ): SettingsViewHolder<T, NumberSettings<T>>

    protected abstract class SettingsViewHolder<T : Number, TSettings : NumberSettings<T>>(
        itemView: View,
    ) : VariableWidgetSettingsViewHolder<T, TSettings>(itemView) {

        protected val binding = ItemVariableNumberSettingsBinding.bind(itemView)

        private var incrementalStepTextWatcher: TextWatcher? = null

        override fun bind(
            settings: TSettings,
            onSettingsChanged: (TSettings) -> Unit,
        ) = with(binding.variableNumberSettingsIncrementStep) {
            removeTextChangedListener(incrementalStepTextWatcher)

            setText(settings.incrementStep.toString())

            incrementalStepTextWatcher = addTextChangedListener { text ->
                onSettingsChanged.invoke(
                    onIncrementalStepChanged(text?.toString().orEmpty())
                )
            }
        }

        protected abstract fun onIncrementalStepChanged(step: String): TSettings
    }

    protected abstract class ViewHolder<T : Number>(
        itemView: View,
    ) : VariableWidgetViewHolder<T>(itemView) {

        protected val binding = ItemVariableStringBinding.bind(itemView)

        private var valueTextWatcher: TextWatcher? = null

        override fun bind(
            item: VariableItem<T>,
            onValueChanged: (T) -> Unit
        ) = with(binding.inputLayout) {
            editText?.removeTextChangedListener(valueTextWatcher)

            hint = item.name
            editText?.setText(item.value.toString())

            valueTextWatcher = editText?.addTextChangedListener { text ->
                onValueChanged.invoke(mapValue(text?.toString().orEmpty()))
            }
        }

        protected abstract fun mapValue(text: String): T

//        protected open fun updateSettings(
//            item: VariableItem,
//            settings: VariableSettings
//        ) = with(binding) {
//            settingsGroup.isGone = true
//
//            buttonSettings.setOnClickListener(null)
//            buttonSettings.isVisible = settingsSupported
//
//            if (settingsSupported) {
//                buttonSettings.setOnClickListener {
//                    settingsGroup.isVisible = settingsGroup.isGone
//                }
//            }
//        }
    }
}
