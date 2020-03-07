package com.redmadrobot.debug_sample

import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleWrapper

class FeatureToggleWrapperImpl : FeatureToggleWrapper {
    companion object {
        val toggleNames = listOf("f1", "f2")
    }

    override fun toggleValue(name: String): Boolean {
        return name == toggleNames.firstOrNull()
    }
}
