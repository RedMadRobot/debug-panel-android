package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.data.accounts.model.DebugUserCredentials
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleWrapper
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.internal.DebugPanelConfig

class App : Application(), Authenticator {
    private val featureToggles = listOf("f1", "f2")

    lateinit var featureToggleWrapper: FeatureToggleWrapper

    override fun onCreate() {
        super.onCreate()

        val debugPanelConfig = DebugPanelConfig(
            application = this,
            //TODO Временная реализация. Здесь это не должно делаться.
            authenticator = this
        )

        DebugPanel.initialize(debugPanelConfig)
        featureToggleWrapper = DebugPanel.initFeatureToggles(
            FeatureTogglesConfig(featureToggles),
            FeatureToggleWrapper { true }
        )
    }

    override fun authenticate(userCredentials: DebugUserCredentials) {
        println("Login - ${userCredentials.login}, Password - ${userCredentials.password}")
    }
}
