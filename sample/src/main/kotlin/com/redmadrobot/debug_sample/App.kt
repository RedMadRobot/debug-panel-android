package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug_panel.DebugPanel

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugPanel().start(this)
    }
}
