package com.redmadrobot.flipper_plugin.ui.item

import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue

internal data class FlipperFeatureItem(
    val feature: Feature,
    val value: FlipperValue,
)
