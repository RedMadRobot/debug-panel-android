package com.redmadrobot.debug_panel.ui.settings.item

import android.view.View
import androidx.core.view.isVisible
import com.redmadrobot.debug_panel.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_preference_value.view.*
import timber.log.Timber

class PreferenceValueItem(
    private val key: String,
    private var value: Any?,
    private val onChanged: (key: String, newValue: Any) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_preference_value

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder.containerView) {
            setting_label.text = key
            setting_type.text = value?.let { it::class.java.simpleName }
            setting_type.isVisible = value != null

            setSettingValue()
            save_value.setOnClickListener { saveNewValue() }

        }
    }

    private fun View.saveNewValue() {
        val fieldValue = setting_value.text.toString()
        try {
            val settingValue = castToNeededType(fieldValue)
            onChanged.invoke(key, settingValue)
            setting_value.clearFocus()

            this@PreferenceValueItem.value = settingValue
            setting_value_container.error = null
            notifyChanged()
        } catch (e: Exception) {
            Timber.e(e)
            setting_value_container.error = context.getString(R.string.wrong_type)
        }
    }

    private fun View.setSettingValue() {
        setting_value.setText(value.toString())
        setting_value.clearFocus()

        setting_value.setOnFocusChangeListener { _, hasFocus ->
            save_value.isVisible = hasFocus && value != null
        }
    }

    private fun castToNeededType(newValue: String): Any {
        return when (value) {
            is Long -> newValue.toLong()
            is String -> newValue
            is Float -> newValue.toFloat()
            is Int -> newValue.toInt()
            else -> throw Throwable("Unexpected type")
        }
    }
}

