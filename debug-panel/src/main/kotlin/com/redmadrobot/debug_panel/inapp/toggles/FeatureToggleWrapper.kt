package com.redmadrobot.debug_panel.inapp.toggles

interface FeatureToggleWrapper {

    companion object {
        operator fun invoke(action: (name: String) -> Boolean): FeatureToggleWrapper {
            return object : FeatureToggleWrapper {
                override fun toggleValue(name: String): Boolean {
                    return action.invoke(name)
                }
            }
        }
    }

    fun toggleValue(name: String): Boolean
}
