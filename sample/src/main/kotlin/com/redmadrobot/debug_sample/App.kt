package com.redmadrobot.debug_sample

import android.app.Application
import com.redmadrobot.debug_panel.DebugPanel
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleWrapper

class App : Application() {
    private val featureToggles = listOf("f1", "f2")

    lateinit var featureToggleWrapper: FeatureToggleWrapper

    override fun onCreate() {
        super.onCreate()
        DebugPanel(this).apply {
            start()
            featureToggleWrapper = initFeatureToggles(
                FeatureTogglesConfig(featureToggles),
                FeatureToggleWrapper { true }
            )
        }
    }
}
