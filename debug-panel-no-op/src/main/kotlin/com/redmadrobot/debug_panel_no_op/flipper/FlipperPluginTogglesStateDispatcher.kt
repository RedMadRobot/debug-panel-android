package com.redmadrobot.flipper_plugin.plugin

import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public class FlipperPluginTogglesStateDispatcher {

    public fun observeUpdatedToggle(): Flow<Pair<Feature, FlipperValue>> {
        return emptyFlow()
    }

    public suspend fun getSavedTogglesStates(): Map<Feature, FlipperValue> {
        return emptyMap()
    }
}
