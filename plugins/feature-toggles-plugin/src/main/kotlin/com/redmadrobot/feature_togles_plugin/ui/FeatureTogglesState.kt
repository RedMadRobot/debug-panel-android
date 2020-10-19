package com.redmadrobot.feature_togles_plugin.ui

import com.xwray.groupie.kotlinandroidextensions.Item

data class FeatureTogglesState(
    val overrideEnable: Boolean,
    val featureToggleItems: List<Item>
)
