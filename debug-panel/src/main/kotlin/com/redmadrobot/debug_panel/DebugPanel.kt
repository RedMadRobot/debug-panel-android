package com.redmadrobot.debug_panel

import android.content.Context
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.util.ActivityLifecycleHandler

object DebugPanel {

    internal var authenticator: Authenticator? = null


    fun start(context: Context): DebugPanel {
        ActivityLifecycleHandler(context).register()
        return this
    }

    fun setAuthenticator(authenticator: Authenticator): DebugPanel {
        this.authenticator = authenticator
        return this
    }

}
