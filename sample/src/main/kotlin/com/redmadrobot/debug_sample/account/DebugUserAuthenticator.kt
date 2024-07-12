package com.redmadrobot.debug_sample.account

import com.redmadrobot.debug.plugin.accounts.authenticator.DebugAuthenticator
import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount
import timber.log.Timber

class DebugUserAuthenticator : DebugAuthenticator {

    override fun onAccountSelected(account: DebugAccount) {
        Timber.i("Login - ${account.login}, Password - ${account.password} Pin - ${account.pin}")
    }
}
