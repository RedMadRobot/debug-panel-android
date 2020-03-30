package com.redmadrobot.debug_panel.inapp.toggles

import com.redmadrobot.debug_panel.data.toggles.LocalFeatureToggleRepository

class FeatureToggleHolder(
    private val localFeatureToggleRepository: LocalFeatureToggleRepository
) {

    private var featureTogglesConfig: FeatureTogglesConfig? = null

    fun initConfig(featureTogglesConfig: FeatureTogglesConfig) {
        this.featureTogglesConfig = featureTogglesConfig
        localFeatureToggleRepository.initConfig(featureTogglesConfig)
    }
}
