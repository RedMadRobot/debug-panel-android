package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.core.DebugPanelConfig
import com.redmadrobot.debug.plugin.aboutapp.AboutAppPlugin
import com.redmadrobot.debug.plugin.appsettings.AppSettingsPlugin
import com.redmadrobot.debug.plugin.konfeature.KonfeatureDebugPanelInterceptor
import com.redmadrobot.debug.plugin.konfeature.KonfeaturePlugin
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug_sample.debug_data.DebugAboutAppInfoProvider
import com.redmadrobot.debug_sample.debug_data.DebugServersProvider
import com.redmadrobot.debug_sample.storage.AppTestSettings
import com.redmadrobot.debug_sample.storage.TestKonfeatureProvider

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val debugPanelInterceptor = KonfeatureDebugPanelInterceptor(this)

        DebugPanel.initialize(
            application = this,
            config = DebugPanelConfig(shakerMode = false),
            plugins = listOf(
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
                KonfeaturePlugin(
                    debugPanelInterceptor = debugPanelInterceptor,
                    konfeature = TestKonfeatureProvider.create(debugPanelInterceptor),
                ),
                AboutAppPlugin(
                    appInfoList = DebugAboutAppInfoProvider.provideData()
                )
            )
        )
    }
}
