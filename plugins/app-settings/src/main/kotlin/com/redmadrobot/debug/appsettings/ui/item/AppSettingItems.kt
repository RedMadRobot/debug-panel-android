package com.redmadrobot.debug.appsettings.ui.item

import androidx.core.view.isVisible
import com.redmadrobot.app_settings_plugin.R
import com.redmadrobot.app_settings_plugin.databinding.ItemHeaderBinding
import com.redmadrobot.app_settings_plugin.databinding.ItemPreferenceBooleanBinding
import com.redmadrobot.app_settings_plugin.databinding.ItemPreferenceValueBinding
import com.redmadrobot.itemsadapter.ItemsAdapter
import com.redmadrobot.itemsadapter.bind
import timber.log.Timber

internal sealed class AppSettingItems {

    abstract fun getItem(bindingContext: ItemsAdapter.BindingContext): ItemsAdapter.Item

    internal class Header(private val header: String) : AppSettingItems() {
        override fun getItem(bindingContext: ItemsAdapter.BindingContext): ItemsAdapter.Item {
            return bindingContext.bind<ItemHeaderBinding>(R.layout.item_header) {
                itemHeaderText.text = header
            }
        }
    }

    class ValueItem(
        private val key: String,
        var value: Any?,
        private val onChanged: (key: String, newValue: Any) -> Unit
    ) : AppSettingItems() {
        override fun getItem(bindingContext: ItemsAdapter.BindingContext): ItemsAdapter.Item {
            return bindingContext.bind<ItemPreferenceValueBinding>(R.layout.item_preference_value) {
                settingLabel.text = key
                settingType.text = value?.let { it::class.java.simpleName }
                settingType.isVisible = value != null

                setSettingValue()
                saveValue.setOnClickListener { saveNewValue() }
            }
        }

        private fun ItemPreferenceValueBinding.setSettingValue() = with(settingValue) {
            setText(value.toString())
            clearFocus()
            settingValueContainer.error = null

            setOnFocusChangeListener { _, hasFocus ->
                saveValue.isVisible = hasFocus && value != null
            }
        }

        private fun ItemPreferenceValueBinding.saveNewValue() {
            val fieldValue = settingValue.text.toString()
            try {
                val newValue = castToNeededType(fieldValue)
                onChanged.invoke(key, newValue)

                value = newValue
                setSettingValue()
            } catch (e: Exception) {
                Timber.e(e)
                settingValueContainer.error = root.context.getString(R.string.wrong_type)
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

    class BooleanValueItem(
        private val key: String,
        var value: Boolean,
        private val onChanged: (key: String, newValue: Any) -> Unit
    ) : AppSettingItems() {

        override fun getItem(bindingContext: ItemsAdapter.BindingContext): ItemsAdapter.Item {
            return bindingContext.bind<ItemPreferenceBooleanBinding>(R.layout.item_preference_boolean) {
                settingLabel.text = key
                settingType.text = value::class.java.simpleName

                with(settingSwitch) {
                    setOnCheckedChangeListener(null)
                    isChecked = value
                    setOnCheckedChangeListener { _, isChecked ->
                        onChanged.invoke(key, isChecked)
                        value = isChecked
                    }
                }
            }
        }
    }
}
