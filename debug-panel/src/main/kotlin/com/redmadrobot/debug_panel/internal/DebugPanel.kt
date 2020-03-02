package com.redmadrobot.debug_panel.internal

import com.redmadrobot.debug_panel.accounts.Authenticator

object DebugPanel {

    internal val authenticator: Authenticator?
        get() = debugPanelInstance?.authenticator

    private var debugPanelInstance: DebugPanelInstance? = null

    fun initialize(debugPanelConfig: DebugPanelConfig) {
        debugPanelInstance = DebugPanelInstance(
            application = debugPanelConfig.application,
            authenticator = debugPanelConfig.authenticator
        )
        debugPanelConfig.featureTogglesConfig?.let { config ->
            debugPanelInstance?.getContainer()
                ?.featureToggleHolder
                ?.initConfig(config)
        }
    }

    internal fun getContainer(): DebugPanelContainer {
        return debugPanelInstance?.getContainer()
            ?: throw IllegalStateException("Debug panel must be initialised")
    }
}
