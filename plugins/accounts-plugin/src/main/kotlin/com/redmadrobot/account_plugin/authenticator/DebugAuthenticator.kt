package com.redmadrobot.account_plugin.authenticator

import com.redmadrobot.account_plugin.data.model.DebugAccount
import io.reactivex.Completable

interface DebugAuthenticator {
    fun onAccountSelected(account: DebugAccount): Completable
    fun onPinAdded(pin: String): Completable = Completable.complete()
}
