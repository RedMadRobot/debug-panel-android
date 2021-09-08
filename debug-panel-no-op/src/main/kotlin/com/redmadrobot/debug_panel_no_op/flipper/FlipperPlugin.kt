package com.redmadrobot.flipper_plugin.plugin

import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue

public class FlipperPlugin(
    private val featureStateMap: Map<Feature, FlipperValue> = emptyMap(),
) {

}
