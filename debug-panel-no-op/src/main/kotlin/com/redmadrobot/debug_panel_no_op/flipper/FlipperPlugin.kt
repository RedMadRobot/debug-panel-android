package com.redmadrobot.flipper_plugin.plugin

import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public class FlipperPlugin(
    private val featureStateMap: Map<Feature, FlipperValue> = emptyMap(),
) {

    public companion object {
        public fun observeUpdatedToggle(): Flow<Pair<Feature, FlipperValue>> {
            return emptyFlow()
        }

        public fun observeMultipleTogglesChanged(): Flow<Map<Feature, FlipperValue>> {
            return emptyFlow()
        }
    }
}
