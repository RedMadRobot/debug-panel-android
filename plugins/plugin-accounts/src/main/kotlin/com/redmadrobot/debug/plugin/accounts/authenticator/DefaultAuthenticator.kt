package com.redmadrobot.debug.plugin.accounts.authenticator

import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount

internal class DefaultAuthenticator : DebugAuthenticator {
    override fun onAccountSelected(account: DebugAccount) = Unit
}
