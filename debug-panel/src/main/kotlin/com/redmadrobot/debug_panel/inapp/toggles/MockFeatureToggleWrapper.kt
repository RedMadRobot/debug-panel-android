package com.redmadrobot.debug_panel.inapp.toggles


class MockFeatureToggleWrapper(
    private val featureTogglesConfig: FeatureTogglesConfig
) : FeatureToggleWrapper {
    var featureToggles = listOf<FeatureToggle>()
        internal set

    override fun toggleValue(name: String): Boolean {
        return featureToggles.firstOrNull { it.name == name }
            ?.value
            ?: featureTogglesConfig.defaultValue
    }
}
