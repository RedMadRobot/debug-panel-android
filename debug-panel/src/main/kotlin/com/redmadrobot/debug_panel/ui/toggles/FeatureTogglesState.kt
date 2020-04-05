package com.redmadrobot.debug_panel.ui.toggles

import com.xwray.groupie.kotlinandroidextensions.Item

data class FeatureTogglesState(
    val overrideEnable: Boolean,
    val featureToggleItems: List<Item>
)
