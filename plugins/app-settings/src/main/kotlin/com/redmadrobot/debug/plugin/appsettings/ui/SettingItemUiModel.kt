package com.redmadrobot.debug.plugin.appsettings.ui

internal sealed class SettingItemUiModel {

    data class Header(val header: String) : SettingItemUiModel()
    data class ValueItem(
        val key: String,
        var value: Any?,
    ) : SettingItemUiModel() {
        fun castToNeededType(newValue: String): Any {
            return when (value) {
                is Long -> newValue.toLong()
                is String -> newValue
                is Float -> newValue.toFloat()
                is Int -> newValue.toInt()
                else -> error("Unexpected type")
            }
        }
    }

    data class BooleanItem(val key: String, var value: Boolean) : SettingItemUiModel()
}
