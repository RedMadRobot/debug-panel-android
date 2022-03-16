package com.redmadrobot.flipper_plugin.ui.data

import com.redmadrobot.flipper.config.FlipperValue

internal sealed interface FlipperFeature {

    data class Group(
        val name: String,
        val allEnabled: Boolean,
        val editable: Boolean,
    ) : FlipperFeature

    data class Item(
        val id: String,
        val value: FlipperValue,
        val description: String,
        val editable: Boolean,
    ) : FlipperFeature
}
