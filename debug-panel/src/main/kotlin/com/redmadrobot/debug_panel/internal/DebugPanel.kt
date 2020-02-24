package com.redmadrobot.debug_panel.internal

import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleHolder
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleWrapper
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig

object DebugPanel {

    internal val authenticator: Authenticator?
        get() = debugPanelInstance?.authenticator

    private var debugPanelInstance: DebugPanelInstance? = null

    fun initialize(debugPanelConfig: DebugPanelConfig) {
        this.debugPanelInstance = DebugPanelInstance(
            application = debugPanelConfig.application,
            authenticator = debugPanelConfig.authenticator
        )
    }

    fun initFeatureToggles(
        featureTogglesConfig: FeatureTogglesConfig,
        featureToggleWrapper: FeatureToggleWrapper
    ): FeatureToggleWrapper {
        return FeatureToggleHolder.init(featureTogglesConfig, featureToggleWrapper)
    }

    internal fun getContainer(): DebugPanelContainer {
        return debugPanelInstance?.getContainer()
            ?: throw IllegalStateException("Debug panel must be initialised")
    }
}
