package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.core.DebugPanelConfig
import com.redmadrobot.debug.plugin.accounts.AccountsPlugin
import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount
import com.redmadrobot.debug.plugin.appsettings.AppSettingsPlugin
import com.redmadrobot.debug.plugin.flipper.FlipperPlugin
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.variable.VariablePlugin
import com.redmadrobot.debug_sample.account.DebugUserAuthenticator
import com.redmadrobot.debug_sample.debug_data.DebugAccountsProvider
import com.redmadrobot.debug_sample.debug_data.DebugFlipperFeaturesProvider
import com.redmadrobot.debug_sample.debug_data.DebugServersProvider
import com.redmadrobot.debug_sample.debug_data.DebugVariableWidgetsProvider
import com.redmadrobot.debug_sample.storage.AppTestSettings

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        DebugPanel.initialize(
            application = this,
            config = DebugPanelConfig(shakerMode = false),
            plugins = listOf(
                AccountsPlugin(
                    preInstalledAccounts = DebugAccountsProvider().provideData(),
                    debugAuthenticator = DebugUserAuthenticator()
                ),
                ServersPlugin(
                    preInstalledServers = DebugServersProvider().provideData()
                ),
                AppSettingsPlugin(
                    sharedPreferences = listOf(
                        AppTestSettings(this.applicationContext).testSharedPreferences,
                        AppTestSettings(this.applicationContext).testSharedPreferences,
                        AppTestSettings(this.applicationContext).testSharedPreferences
                    )
                ),
                FlipperPlugin(
                    toggles = DebugFlipperFeaturesProvider().provideData(),
                ),
                VariablePlugin(
                    customWidgets = DebugVariableWidgetsProvider().provideData()
                ),
//                , FeatureTogglesPlugin(
//                    featureTogglesConfig = FeatureTogglesConfig(
//                        FeatureToggleWrapperImpl.toggleNames,
//                        FeatureToggleWrapperImpl(),
//                        this
//                    )
//                )
            )
        )
    }


    private fun getPreInstalledServers(): List<DebugServer> {
        return listOf(
            DebugServer(name = "debug 1", url = "https://testserver1.com")
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
                password = "Ww!11111",
                pin = "112"
            ),
            DebugAccount(
                login = "1944647499",
                password = "Qq!11111",
                pin = "1111"
            )
        )
    }
}
