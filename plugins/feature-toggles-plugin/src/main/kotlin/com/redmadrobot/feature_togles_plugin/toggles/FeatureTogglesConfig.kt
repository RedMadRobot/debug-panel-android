package com.redmadrobot.feature_togles_plugin.toggles

data class FeatureTogglesConfig(
    val toggleNames: List<String>,
    val featureToggleWrapper: FeatureToggleWrapper,
    val changeListener: FeatureToggleChangeListener
)
