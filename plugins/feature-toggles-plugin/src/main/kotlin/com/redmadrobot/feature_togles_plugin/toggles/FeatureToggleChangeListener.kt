package com.redmadrobot.feature_togles_plugin.toggles

interface FeatureToggleChangeListener {

    fun onFeatureToggleChange(name: String, newValue: Boolean)
}
