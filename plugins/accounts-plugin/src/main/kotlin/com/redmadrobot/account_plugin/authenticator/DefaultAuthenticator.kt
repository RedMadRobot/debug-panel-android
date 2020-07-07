package com.redmadrobot.account_plugin.authenticator

import com.redmadrobot.core.data.storage.entity.DebugAccount
import io.reactivex.Completable

internal class DefaultAuthenticator : DebugAuthenticator {
    override fun onAccountSelected(account: DebugAccount) = Completable.complete()

    override fun onPinAdded(pin: String) = Completable.complete()
}
