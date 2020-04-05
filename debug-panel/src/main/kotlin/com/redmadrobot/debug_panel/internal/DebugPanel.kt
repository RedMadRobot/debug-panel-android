package com.redmadrobot.debug_panel.internal

import android.app.Application
import com.redmadrobot.debug_panel.accounts.Authenticator

object DebugPanel {

    internal val authenticator: Authenticator?
        get() = debugPanelInstance?.debugPanelConfig?.authenticator

    private var debugPanelInstance: DebugPanelInstance? = null

    fun initialize(application: Application, debugPanelConfig: DebugPanelConfig) {
        debugPanelInstance = DebugPanelInstance(application,debugPanelConfig)
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
