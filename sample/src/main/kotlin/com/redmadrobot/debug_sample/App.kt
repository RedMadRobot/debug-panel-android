package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.plugin.AccountsPlugin
import com.redmadrobot.app_settings_plugin.plugin.AppSettingsPlugin
import com.redmadrobot.debug_panel_core.internal.DebugPanel
import com.redmadrobot.debug_sample.account.DebugUserAuthenticator
import com.redmadrobot.debug_sample.debug_data.DebugAccountsProvider
import com.redmadrobot.debug_sample.debug_data.DebugServersProvider
import com.redmadrobot.debug_sample.storage.AppTestSettings
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.plugin.ServersPlugin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        DebugPanel.initialize(
            application = this,
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
                )
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
