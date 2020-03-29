package com.redmadrobot.debug_panel.inapp.toggles

class FeatureToggleHolder {

    private var featureTogglesConfig: FeatureTogglesConfig? = null
    private var featureToggles: List<FeatureToggle> = emptyList()

    fun initConfig(featureTogglesConfig: FeatureTogglesConfig) {
        this.featureTogglesConfig = featureTogglesConfig
        featureToggles = featureTogglesConfig.toggleNames.map { name ->
            FeatureToggle(name, featureTogglesConfig.featureToggleWrapper.toggleValue(name))
        }
    }

    fun resetAll() {
        featureTogglesConfig?.let { config ->
            config.toggleNames.forEach { toggleName ->
                val newValue = featureToggles.firstOrNull { it.name == toggleName }?.value
                val defaultValue = config.featureToggleWrapper.toggleValue(toggleName)
                if (newValue != defaultValue) {
                    updateFeatureToggle(toggleName, defaultValue)
                }
            }
        }
    }

    fun getFeatureToggles(): List<FeatureToggle> {
        return featureTogglesConfig?.let { featureToggles }
            ?: throw IllegalStateException("Must be set featureTogglesConfig in DebugPanelConfig")
    }

    fun updateFeatureToggle(toggleName: String, newValue: Boolean) {
        featureTogglesConfig?.changeListener?.onFeatureToggleChange(toggleName, newValue)
            ?: throw IllegalStateException("Must be set featureTogglesConfig in DebugPanelConfig")
        featureToggles = featureToggles.map { featureToggle ->
            if (featureToggle.name == toggleName) {
                featureToggle.copy(value = newValue)
            } else {
                featureToggle
            }
        }
    }
}
