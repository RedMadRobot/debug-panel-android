package com.redmadrobot.flipper_plugin.plugin

import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import kotlinx.coroutines.flow.Flow

public object FlipperPluginTogglesStateDispatcher {

    private val featureTogglesRepository by lazy(LazyThreadSafetyMode.NONE) {
        getPlugin<FlipperPlugin>()
            .getContainer<FlipperPluginContainer>()
            .featureTogglesRepository
    }

    public fun observeUpdatedToggle(): Flow<Pair<Feature, FlipperValue>> {
        return featureTogglesRepository.observeUpdatedToggle()
    }

    public fun observeMultipleTogglesChanged(): Flow<Map<Feature, FlipperValue>> {
        return featureTogglesRepository.observeMultipleTogglesChanged()
    }
}
