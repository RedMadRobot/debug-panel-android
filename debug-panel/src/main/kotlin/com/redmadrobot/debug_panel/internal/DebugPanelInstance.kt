package com.redmadrobot.debug_panel.internal

import android.app.Application
import android.content.Context
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.util.ActivityLifecycleHandler

internal class DebugPanelInstance(
    application: Application,
    internal val authenticator: Authenticator
) {

    private var debugPanelContainer: DebugPanelContainer? = null

    init {
        ActivityLifecycleHandler(application).start()
        initContainer(application.applicationContext)
    }

    internal fun getContainer(): DebugPanelContainer {
        return debugPanelContainer
            ?: throw IllegalStateException("Container not initialised")
    }

    private fun initContainer(context: Context) {
        debugPanelContainer = DebugPanelContainer(context)
    }
}
