package com.redmadrobot.debug.accounts.authenticator

import com.redmadrobot.debug.accounts.data.model.DebugAccount

internal class DefaultAuthenticator : DebugAuthenticator {
    override fun onAccountSelected(account: DebugAccount) = Unit
}
