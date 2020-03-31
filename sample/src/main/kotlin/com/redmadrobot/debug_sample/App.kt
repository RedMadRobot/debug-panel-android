package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.data.accounts.model.DebugUserCredentials
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleChangeListener
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.internal.DebugPanelConfig

class App : Application(), Authenticator, FeatureToggleChangeListener {
    override fun onCreate() {
        super.onCreate()

        val debugPanelConfig = DebugPanelConfig(
            application = this,
            //TODO Временная реализация. Здесь это не должно делаться.
            authenticator = this,
            featureTogglesConfig = FeatureTogglesConfig(
                FeatureToggleWrapperImpl.toggleNames,
                FeatureToggleWrapperImpl(),
                this
            )
        )

        DebugPanel.initialize(debugPanelConfig)
    }

    override fun authenticate(userCredentials: DebugUserCredentials) {
        println("Login - ${userCredentials.login}, Password - ${userCredentials.password}")
    }

    override fun onFeatureToggleChange(name: String, newValue: Boolean) {
        // Feature toggle was changed. You need
        println("New value for key \"$name\" = $newValue")
    }
}
