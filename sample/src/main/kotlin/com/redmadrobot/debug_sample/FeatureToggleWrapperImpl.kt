package com.redmadrobot.debug_sample

import com.redmadrobot.feature_togles_plugin.toggles.FeatureToggleWrapper

class FeatureToggleWrapperImpl : FeatureToggleWrapper {
    companion object {
        val toggleNames = listOf("f1", "f2")
    }

    override fun toggleValue(name: String): Boolean {
        return name == toggleNames.firstOrNull()
    }
}
