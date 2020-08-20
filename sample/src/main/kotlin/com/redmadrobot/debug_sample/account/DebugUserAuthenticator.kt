package com.redmadrobot.debug_sample.account

import com.redmadrobot.account_plugin.authenticator.DebugAuthenticator
import com.redmadrobot.account_plugin.data.model.DebugAccount
import timber.log.Timber

class DebugUserAuthenticator : DebugAuthenticator {

    override fun onAccountSelected(account: DebugAccount) {
        Timber.i("Login - ${account.login}, Password - ${account.password} Pin - ${account.pin}")
    }
}
