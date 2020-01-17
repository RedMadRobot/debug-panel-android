package com.redmadrobot.debug_panel.internal

import android.content.Context
import com.redmadrobot.debug_panel.accounts.Authenticator
import com.redmadrobot.debug_panel.accounts.DefaultAuthenticator

data class DebugPanelConfig(
    val context: Context,
    val authenticator: Authenticator = DefaultAuthenticator()
)
