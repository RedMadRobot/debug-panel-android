package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug_panel.DebugPanel
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials

class App : Application(), Authenticator {

    override fun onCreate() {
        super.onCreate()
        DebugPanel.start(this)
            //TODO Временная реализация. Здесь это не должно делаться.
            .setAuthenticator(this)
    }

    override fun authenticate(userCredentials: DebugUserCredentials) {
        println("Login - ${userCredentials.login}, Password - ${userCredentials.password}")
    }
}
