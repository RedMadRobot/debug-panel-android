package com.redmadrobot.debug.plugin.flipper.ui.data

import com.redmadrobot.flipper.config.FlipperValue

internal sealed interface FlipperItem {

    data class Group(
        val name: String,
        val allEnabled: Boolean,
        val editable: Boolean,
    ) : FlipperItem

    data class Feature(
        val id: String,
        val value: FlipperValue,
        val description: String,
        val editable: Boolean,
    ) : FlipperItem
}
