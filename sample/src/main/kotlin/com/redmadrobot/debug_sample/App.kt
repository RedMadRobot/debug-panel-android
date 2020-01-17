package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug_panel.DebugPanel
import com.redmadrobot.debug_panel.DebugPanelController

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugPanel(this).start()
    }
}
