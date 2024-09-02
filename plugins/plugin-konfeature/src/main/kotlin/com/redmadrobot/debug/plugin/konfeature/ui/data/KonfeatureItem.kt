package com.redmadrobot.debug.plugin.konfeature.ui.data

import androidx.compose.ui.graphics.Color

internal sealed interface KonfeatureItem {

    data class Config(
        val name: String,
        val description: String,
    ) : KonfeatureItem

    data class Value(
        val key: String,
        val configName: String,
        val value: Any,
        val description: String,
        val sourceName: String,
        val sourceColor: Color,
        val isDebugSource: Boolean
    ) : KonfeatureItem {

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
