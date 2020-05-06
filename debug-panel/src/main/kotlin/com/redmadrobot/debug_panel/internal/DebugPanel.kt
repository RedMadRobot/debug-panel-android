package com.redmadrobot.debug_panel.internal

import android.app.Application
import com.redmadrobot.debug_panel.accounts.Authenticator
import timber.log.Timber

object DebugPanel {

    internal val authenticator: Authenticator?
        get() = debugPanelInstance?.debugPanelConfig?.authenticator

    private var debugPanelInstance: DebugPanelInstance? = null

    fun initialize(application: Application, debugPanelConfig: DebugPanelConfig) {
        createDebugPanelInstance(application, debugPanelConfig)
        initTimber()
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

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun createDebugPanelInstance(
        application: Application,
        debugPanelConfig: DebugPanelConfig
    ) {
        debugPanelInstance = DebugPanelInstance(application, debugPanelConfig)
    }
}
