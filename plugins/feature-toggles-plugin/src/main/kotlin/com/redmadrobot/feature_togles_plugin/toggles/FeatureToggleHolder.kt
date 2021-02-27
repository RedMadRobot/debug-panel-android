package com.redmadrobot.feature_togles_plugin.toggles

import com.redmadrobot.feature_togles_plugin.data.FeatureToggleRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FeatureToggleHolder(private val localFeatureToggleRepository: FeatureToggleRepository) {

    private var featureTogglesConfig: FeatureTogglesConfig? = null

    fun initConfig(featureTogglesConfig: FeatureTogglesConfig) {
        this.featureTogglesConfig = featureTogglesConfig
        GlobalScope.launch { localFeatureToggleRepository.initConfig(featureTogglesConfig) }
    }
}
