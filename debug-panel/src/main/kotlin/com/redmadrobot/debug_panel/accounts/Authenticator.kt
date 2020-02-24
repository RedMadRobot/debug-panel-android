package com.redmadrobot.debug_panel.accounts

import com.redmadrobot.debug_panel.data.accounts.model.DebugUserCredentials

interface Authenticator {
    fun authenticate(userCredentials: DebugUserCredentials)
}
