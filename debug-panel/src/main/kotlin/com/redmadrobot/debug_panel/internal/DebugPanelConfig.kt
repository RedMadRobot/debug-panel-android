package com.redmadrobot.debug_panel.internal

import android.app.Application
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.accounts.DefaultAuthenticator
import com.redmadrobot.debug_panel.inapp.toggles.FeatureTogglesConfig

data class DebugPanelConfig(
    val application: Application,
    val authenticator: Authenticator = DefaultAuthenticator(),
    val featureTogglesConfig: FeatureTogglesConfig? = null
)
