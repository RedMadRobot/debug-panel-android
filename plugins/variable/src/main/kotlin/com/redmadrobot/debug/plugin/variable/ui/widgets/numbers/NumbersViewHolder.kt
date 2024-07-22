package com.redmadrobot.debug.plugin.variable.ui.widgets.numbers

import android.os.Build
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.redmadrobot.debug.plugin.variable.extensions.toNumberSafe
import com.redmadrobot.debug.plugin.variable.VariableItem
import com.redmadrobot.debug.plugin.variable.VariableWidgetViewHolder
import com.redmadrobot.debug.plugin.variable.databinding.ItemVariableNumberBinding
import java.util.Locale
import kotlin.reflect.KClass

internal open class NumbersViewHolder<T : Number>(
    itemView: View,
    private val kClass: KClass<T>,
) : VariableWidgetViewHolder<T>(itemView) {

    protected val binding = ItemVariableNumberBinding.bind(itemView)

    private var valueTextWatcher: TextWatcher? = null

    init {
        binding.inputLayout.editText?.filters = getInputFilters()
    }

    override fun bind(
        item: VariableItem<T>,
        onValueChanged: (T) -> Unit
    ) = with(binding.inputLayout) {
        editText?.removeTextChangedListener(valueTextWatcher)

        hint = item.name
        editText?.setText(item.value.toString())

        valueTextWatcher = editText?.addTextChangedListener { text ->
            onValueChanged.invoke(
                text?.toString().orEmpty().toNumberSafe(kClass)
            )
        }
    }

    protected open fun getInputFilters(): Array<InputFilter> {
        val shouldAllowDecimal = kClass == Double::class || kClass == Float::class
        val digitsFilter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DigitsKeyListener.getInstance(
                Locale.getDefault(),
                true,
                shouldAllowDecimal
            )
        } else {
            DigitsKeyListener.getInstance(true, shouldAllowDecimal)
        }

        return arrayOf(digitsFilter)
    }
}
