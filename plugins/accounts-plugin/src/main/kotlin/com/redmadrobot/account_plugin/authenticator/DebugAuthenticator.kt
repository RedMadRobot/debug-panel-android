package com.redmadrobot.account_plugin.authenticator

import com.redmadrobot.account_plugin.data.model.DebugAccount

interface DebugAuthenticator {
    fun onAccountSelected(account: DebugAccount)
}
