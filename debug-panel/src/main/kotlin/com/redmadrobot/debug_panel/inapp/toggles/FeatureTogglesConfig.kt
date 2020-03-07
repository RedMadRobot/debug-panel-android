package com.redmadrobot.debug_panel.inapp.toggles

data class FeatureTogglesConfig(
    val toggleNames: List<String>,
    val featureToggleWrapper: FeatureToggleWrapper,
    val changeListener: FeatureToggleChangeListener
)
