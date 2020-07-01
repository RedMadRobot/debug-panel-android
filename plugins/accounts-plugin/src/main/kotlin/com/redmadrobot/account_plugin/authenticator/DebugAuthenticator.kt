package com.redmadrobot.account_plugin.authenticator

import com.redmadrobot.core.data.storage.entity.DebugAccount
import io.reactivex.Completable

interface DebugAuthenticator {
    fun onAccountSelected(account: DebugAccount): Completable
    fun onPinAdded(pin: String): Completable
}
