package com.redmadrobot.feature_togles_plugin.toggles

interface FeatureToggleWrapper {

    fun toggleValue(name: String): Boolean
}
