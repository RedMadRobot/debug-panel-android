package com.redmadrobot.debug.account.authenticator

import com.redmadrobot.debug.account.data.model.DebugAccount

internal class DefaultAuthenticator : DebugAuthenticator {
    override fun onAccountSelected(account: DebugAccount) = Unit
}
