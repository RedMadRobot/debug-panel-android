package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.data.PreInstalledData
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleChangeListener
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.internal.DebugPanelConfig
import com.redmadrobot.debug_sample.storage.AppTestSettings

class App : Application(), Authenticator, FeatureToggleChangeListener {
    override fun onCreate() {
        super.onCreate()

        val debugPanelConfig = DebugPanelConfig(
            //TODO Временная реализация. Здесь это не должно делаться.
            authenticator = this,
            preInstalledServers = PreInstalledData(getPreinstalledServers()),
            preInstalledAccounts = PreInstalledData(getPreInstalledAccounts()),
            featureTogglesConfig = FeatureTogglesConfig(
                FeatureToggleWrapperImpl.toggleNames,
                FeatureToggleWrapperImpl(),
                this
            ),
            sharedPreferences = listOf(
                AppTestSettings(this.applicationContext).testSharedPreferences,
                AppTestSettings(this.applicationContext).testSharedPreferences,
                AppTestSettings(this.applicationContext).testSharedPreferences
            )
        )


        DebugPanel.initialize(this, debugPanelConfig)
    }

    override fun authenticate(account: DebugAccount) {
        println("Login - ${account.login}, Password - ${account.password}")
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

    private fun getPreInstalledAccounts(): List<DebugAccount> {
        return listOf(
            DebugAccount(
                login = "7882340482",
                password = "Qq!11111"
            ),
            DebugAccount(
                login = "2777248041",
                password = "Qq!11111"
            ),
            DebugAccount(
                login = "4183730054",
                password = "Ww!11111"
            ),
            DebugAccount(
                login = "1944647499",
                password = "Qq!11111"
            )
        )
    }
}
