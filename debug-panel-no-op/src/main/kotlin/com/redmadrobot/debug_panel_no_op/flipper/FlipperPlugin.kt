package com.redmadrobot.flipper_plugin.plugin

import com.redmadrobot.flipper.config.FlipperValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public class FlipperPlugin(
    private val featureStateMap: Map<String, FlipperValue> = emptyMap(),
) {

    public companion object {
        public fun observeChangedToggles(): Flow<Map<String, FlipperValue>> {
            return emptyFlow()
        }
    }
}
