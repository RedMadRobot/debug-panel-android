package com.redmadrobot.debug.account.authenticator

import com.redmadrobot.debug.account.data.model.DebugAccount

interface DebugAuthenticator {
    fun onAccountSelected(account: DebugAccount)
}
