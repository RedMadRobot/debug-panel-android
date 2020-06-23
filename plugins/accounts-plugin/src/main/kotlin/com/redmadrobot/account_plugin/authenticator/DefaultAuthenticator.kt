package com.redmadrobot.account_plugin.authenticator

import com.redmadrobot.core.data.storage.entity.DebugAccount

class DefaultAuthenticator : Authenticator {
    override fun authenticate(account: DebugAccount, pin: String?) {
        //Do nothing
    }
}
