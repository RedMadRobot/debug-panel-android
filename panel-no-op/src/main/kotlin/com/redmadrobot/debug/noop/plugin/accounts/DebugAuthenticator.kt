package com.redmadrobot.debug.plugin.accounts.authenticator

import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount

interface DebugAuthenticator {
    fun onAccountSelected(account: DebugAccount)
}
