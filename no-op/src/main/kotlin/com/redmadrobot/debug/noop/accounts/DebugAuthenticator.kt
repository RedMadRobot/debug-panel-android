package com.redmadrobot.debug.accounts.authenticator

import com.redmadrobot.debug.accounts.data.model.DebugAccount

interface DebugAuthenticator {
    fun onAccountSelected(account: DebugAccount)
}
