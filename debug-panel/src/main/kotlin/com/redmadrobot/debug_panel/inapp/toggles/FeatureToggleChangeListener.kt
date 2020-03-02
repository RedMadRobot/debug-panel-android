package com.redmadrobot.debug_panel.inapp.toggles

interface FeatureToggleChangeListener {

    fun onFeatureToggleChange(name: String, newValue: Boolean)
}
