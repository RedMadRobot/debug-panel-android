package com.redmadrobot.debug_panel.internal

import com.redmadrobot.debug_panel.accounts.Authenticator

object DebugPanel {

    internal val authenticator: Authenticator?
        get() = debugPanelInstance?.authenticator

    private var debugPanelInstance: DebugPanelInstance? = null

    fun initialize(debugPanelConfig: DebugPanelConfig) {
        this.debugPanelInstance = DebugPanelInstance(
            context = debugPanelConfig.context,
            authenticator = debugPanelConfig.authenticator
        )
    }

    fun setAuthenticator(authenticator: Authenticator) {
        debugPanelInstance?.let {
            it.authenticator = authenticator
        }
            ?: throw IllegalStateException("The Debug panel must be initialized before calling this method.")
    }

}
