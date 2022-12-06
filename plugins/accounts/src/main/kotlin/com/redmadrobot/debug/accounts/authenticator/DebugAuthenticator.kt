package com.redmadrobot.debug.accounts.authenticator

import com.redmadrobot.debug.accounts.data.model.DebugAccount

public interface DebugAuthenticator {
    public fun onAccountSelected(account: DebugAccount)
}
