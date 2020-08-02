package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.account_plugin.authenticator.DebugAuthenticator
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.plugin.AccountsPlugin
import com.redmadrobot.app_settings_plugin.plugin.AppSettingsPlugin
import com.redmadrobot.core.internal.DebugPanel
import com.redmadrobot.debug_sample.storage.AppTestSettings
import com.redmadrobot.feature_togles_plugin.plugin.FeatureTogglesPlugin
import com.redmadrobot.feature_togles_plugin.toggles.FeatureToggleChangeListener
import com.redmadrobot.feature_togles_plugin.toggles.FeatureTogglesConfig
import com.redmadrobot.servers_plugin.data.model.DebugServer
import com.redmadrobot.servers_plugin.plugin.ServersPlugin
import io.reactivex.Completable

class App : Application(), DebugAuthenticator, FeatureToggleChangeListener {
    override fun onCreate() {
        super.onCreate()

        DebugPanel.initialize(
            this, listOf(
                AccountsPlugin(
                    preInstalledAccounts = getPreInstalledAccounts(),
                    //TODO Временная реализация. Здесь это не должно делаться.
                    debugAuthenticator = this
                ),
                ServersPlugin(
                    preInstalledServers = getPreInstalledServers()
                ),
                AppSettingsPlugin(
                    sharedPreferences = listOf(
                        AppTestSettings(this.applicationContext).testSharedPreferences,
                        AppTestSettings(this.applicationContext).testSharedPreferences,
                        AppTestSettings(this.applicationContext).testSharedPreferences
                    )
                ),
                FeatureTogglesPlugin(
                    featureTogglesConfig = FeatureTogglesConfig(
                        FeatureToggleWrapperImpl.toggleNames,
                        FeatureToggleWrapperImpl(),
                        this
                    )
                )
            )
        )
    }

    private fun onServerChanged(server: DebugServer?) {
        val url = server?.url
        println("Server changed to - $url")
    }

    override fun onAccountSelected(account: DebugAccount): Completable {
        return Completable.fromCallable {
            println("Login - ${account.login}, Password - ${account.password}")
        }
    }

    override fun onPinAdded(pin: String): Completable {
        return Completable.fromCallable {
            println(" Pin - $pin")
        }
    }

    override fun onFeatureToggleChange(name: String, newValue: Boolean) {
        // Feature toggle was changed. You need
        println("New value for key \"$name\" = $newValue")
    }

    private fun getPreInstalledServers(): List<DebugServer> {
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
                password = "Qq!11111",
                pinNeeded = true
            ),
            DebugAccount(
                login = "4183730054",
                password = "Ww!11111",
                pinNeeded = true
            ),
            DebugAccount(
                login = "1944647499",
                password = "Qq!11111",
                pinNeeded = true
            )
        )
    }
}
