package com.redmadrobot.debug_panel.internal

import android.content.Context
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.util.ActivityLifecycleHandler

internal class DebugPanelInstance(
    private val context: Context,
    internal var authenticator: Authenticator
) {

    init {
        ActivityLifecycleHandler(context).register()
    }
}
