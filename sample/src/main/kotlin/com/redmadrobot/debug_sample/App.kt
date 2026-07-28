package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.plugin.aboutapp.AboutAppPlugin
import com.redmadrobot.debug.plugin.konfeature.KonfeatureDebugPanelConfig
import com.redmadrobot.debug.plugin.konfeature.KonfeaturePlugin
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug_sample.debug_data.DebugAboutAppInfoProvider
import com.redmadrobot.debug_sample.debug_data.DebugServersProvider
import com.redmadrobot.debug_sample.storage.TestKonfeatureProvider
import com.redmadrobot.konfeature.Logger
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val konfeatureLogger = konfeatureLogger()
        val debugPanelConfig = runBlocking {
            KonfeatureDebugPanelConfig.create(
                context = this@App,
                logger = konfeatureLogger,
            )
        }

        DebugPanel.initialize(
            application = this,
            plugins = listOf(
                ServersPlugin(
                    preInstalledServers = DebugServersProvider().provideData()
                ),
                KonfeaturePlugin(
                    konfeature = TestKonfeatureProvider.create(debugPanelConfig, konfeatureLogger),
                    config = debugPanelConfig,
                ),
                AboutAppPlugin(
                    appInfoList = DebugAboutAppInfoProvider.provideData()
                )
            )
        )
    }

    private fun konfeatureLogger(): Logger = object : Logger {
        override fun log(severity: Logger.Severity, message: String) {
            when (severity) {
                Logger.Severity.WARNING -> Timber.tag("Konfeature").w(message)
                Logger.Severity.INFO -> Timber.tag("Konfeature").i(message)
            }
        }
    }
}
