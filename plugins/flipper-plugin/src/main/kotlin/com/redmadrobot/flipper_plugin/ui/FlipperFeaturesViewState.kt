package com.redmadrobot.flipper_plugin.ui

import com.redmadrobot.flipper_plugin.ui.data.FlipperFeature

internal data class FlipperFeaturesViewState(
    val featureItems: List<FlipperFeature> = emptyList(),
)
