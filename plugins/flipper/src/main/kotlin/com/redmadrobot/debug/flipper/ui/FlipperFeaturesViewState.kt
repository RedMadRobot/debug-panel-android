package com.redmadrobot.debug.flipper.ui

import com.redmadrobot.debug.flipper.ui.data.FlipperItem

internal data class FlipperFeaturesViewState(
    val items: List<FlipperItem> = emptyList(),
)
