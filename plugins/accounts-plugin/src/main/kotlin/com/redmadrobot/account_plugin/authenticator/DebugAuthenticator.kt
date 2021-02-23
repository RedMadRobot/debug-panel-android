package com.redmadrobot.account_plugin.authenticator

import com.redmadrobot.account_plugin.data.model.DebugAccount

public interface DebugAuthenticator {
    public fun onAccountSelected(account: DebugAccount)
}
