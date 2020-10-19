package com.redmadrobot.account_plugin.authenticator

import com.redmadrobot.account_plugin.data.model.DebugAccount

internal class DefaultAuthenticator : DebugAuthenticator {
    override fun onAccountSelected(account: DebugAccount) = Unit
}
