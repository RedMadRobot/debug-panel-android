package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.internal.DebugPanelConfig

class App : Application(), Authenticator {

    override fun onCreate() {
        super.onCreate()

        val debugPanelConfig = DebugPanelConfig(
            application = this,
            //TODO Временная реализация. Здесь это не должно делаться.
            authenticator = this
        )

        DebugPanel.initialize(debugPanelConfig)
    }

    override fun authenticate(userCredentials: DebugUserCredentials) {
        println("Login - ${userCredentials.login}, Password - ${userCredentials.password}")
    }
}
