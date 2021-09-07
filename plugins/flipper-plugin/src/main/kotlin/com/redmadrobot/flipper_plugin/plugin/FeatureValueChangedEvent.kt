package com.redmadrobot.flipper_plugin.plugin

import com.redmadrobot.debug_panel_core.internal.DebugEvent
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue

public data class FeatureValueChangedEvent(
    val feature: Feature,
    val value: FlipperValue,
) : DebugEvent
