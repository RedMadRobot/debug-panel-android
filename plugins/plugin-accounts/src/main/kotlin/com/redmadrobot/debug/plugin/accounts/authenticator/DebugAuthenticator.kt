package com.redmadrobot.debug.plugin.accounts.authenticator

import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount

public interface DebugAuthenticator {
    public fun onAccountSelected(account: DebugAccount)
}
