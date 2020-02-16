package com.redmadrobot.debug_panel.accounts

import com.redmadrobot.debug_panel.data.accounts.model.DebugUserCredentials

internal class DefaultAuthenticator : Authenticator {
    override fun authenticate(userCredentials: DebugUserCredentials) {
        //Do nothing
    }
}
