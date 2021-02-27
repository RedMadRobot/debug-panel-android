package com.redmadrobot.feature_togles_plugin.data

import com.redmadrobot.feature_togles_plugin.data.model.FeatureToggle
import com.redmadrobot.feature_togles_plugin.toggles.FeatureTogglesConfig

interface FeatureToggleRepository {
    suspend fun initConfig(featureTogglesConfig: FeatureTogglesConfig)
    suspend fun updateFeatureToggle(featureToggle: FeatureToggle)
    suspend fun getAllFeatureToggles(): List<FeatureToggle>
    suspend fun resetAll()
    suspend fun updateOverrideEnable(newOverrideEnable: Boolean)
}
