package com.redmadrobot.debug_panel.inapp.toggles

object FeatureToggleHolder {

    lateinit var mockToggleWrapper: MockFeatureToggleWrapper

    fun init(
        featureTogglesConfig: FeatureTogglesConfig,
        featureToggleWrapper: FeatureToggleWrapper
    ): FeatureToggleWrapper {
        val featureToggles = featureTogglesConfig.toggleNames.map { name ->
            FeatureToggle(name, featureToggleWrapper.toggleValue(name))
        }
        mockToggleWrapper = MockFeatureToggleWrapper(featureTogglesConfig).apply {
            this.featureToggles = featureToggles
        }
        return mockToggleWrapper
    }
}
