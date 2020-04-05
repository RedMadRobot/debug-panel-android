package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.data.PreInstalledData
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import com.redmadrobot.debug_panel.data.storage.entity.DebugUserCredentials
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleChangeListener
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.internal.DebugPanelConfig

class App : Application(), Authenticator, FeatureToggleChangeListener {
    override fun onCreate() {
        super.onCreate()

        val debugPanelConfig = DebugPanelConfig(
            //TODO Временная реализация. Здесь это не должно делаться.
            authenticator = this,
            preInstalledServers = PreInstalledData(getPreinstalledServers()),
            featureTogglesConfig = FeatureTogglesConfig(
                FeatureToggleWrapperImpl.toggleNames,
                FeatureToggleWrapperImpl(),
                this
            )
        )

        DebugPanel.initialize(this, debugPanelConfig)
    }

    override fun authenticate(userCredentials: DebugUserCredentials) {
        println("Login - ${userCredentials.login}, Password - ${userCredentials.password}")
    }

    override fun onFeatureToggleChange(name: String, newValue: Boolean) {
        // Feature toggle was changed. You need
        println("New value for key \"$name\" = $newValue")
    }

    private fun getPreinstalledServers(): List<DebugServer> {
        return listOf(
            DebugServer(url = "https://testserver1.com")
        )
    }
}
