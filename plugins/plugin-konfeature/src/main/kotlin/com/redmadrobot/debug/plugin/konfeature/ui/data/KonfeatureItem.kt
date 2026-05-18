package com.redmadrobot.debug.plugin.konfeature.ui.data

import androidx.compose.ui.graphics.Color

internal sealed interface KonfeatureItem {
    val itemKey: String

    companion object {
        const val ITEM_KEY_PREFIX_CONFIG = "config_"
        const val ITEM_KEY_PREFIX_VALUE = "value_"
    }

    data class Config(
        val name: String,
        val description: String,
    ) : KonfeatureItem {
        override val itemKey: String
            get() = "$ITEM_KEY_PREFIX_CONFIG$name"
    }

    data class Value(
        val key: String,
        val configName: String,
        val value: Any,
        val description: String,
        val sourceName: String,
        val sourceColor: Color,
        val isDebugSource: Boolean
    ) : KonfeatureItem {
        override val itemKey: String
            get() = "$ITEM_KEY_PREFIX_VALUE$key"

        val editAvailable: Boolean
            get() = when (value) {
                is Boolean,
                is String,
                is Long,
                is Double -> true
                else -> false
            }
    }
}
