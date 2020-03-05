package com.redmadrobot.debug_panel.inapp.toggles

interface FeatureToggleWrapper {

    fun toggleValue(name: String): Boolean
}
