package com.redmadrobot.debug_panel.accounts

import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials

interface Authentificator {
    fun authenticate(userCredentials: DebugUserCredentials)
}
