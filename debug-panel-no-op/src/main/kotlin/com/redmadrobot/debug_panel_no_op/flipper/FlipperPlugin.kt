package com.redmadrobot.flipper_plugin.plugin

import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public class FlipperPlugin(
    private val featureStateMap: Map<Feature, FlipperValue> = emptyMap(),
) {

    public companion object {
        public fun observeChangedToggles(): Flow<Map<Feature, FlipperValue>> {
            return emptyFlow()
        }
    }
}
