package com.redmadrobot.debug_panel.accounts

import com.redmadrobot.debug_panel.data.storage.entity.DebugUserCredentials

interface Authenticator {
    fun authenticate(userCredentials: DebugUserCredentials)
}
