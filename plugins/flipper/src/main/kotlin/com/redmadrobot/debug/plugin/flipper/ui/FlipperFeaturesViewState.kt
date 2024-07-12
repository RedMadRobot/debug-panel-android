package com.redmadrobot.debug.plugin.flipper.ui

import com.redmadrobot.debug.plugin.flipper.ui.data.FlipperItem

internal data class FlipperFeaturesViewState(
    val items: List<FlipperItem> = emptyList(),
)
