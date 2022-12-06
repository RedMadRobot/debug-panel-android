package com.redmadrobot.debug.account.authenticator

import com.redmadrobot.debug.account.data.model.DebugAccount

public interface DebugAuthenticator {
    public fun onAccountSelected(account: DebugAccount)
}
