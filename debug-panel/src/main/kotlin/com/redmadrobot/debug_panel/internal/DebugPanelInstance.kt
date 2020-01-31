package com.redmadrobot.debug_panel.internal

import android.app.Application
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.util.ActivityLifecycleHandler

internal class DebugPanelInstance(
    application: Application,
    internal val authenticator: Authenticator
) {

    init {
        ActivityLifecycleHandler(application).register()
    }
}
