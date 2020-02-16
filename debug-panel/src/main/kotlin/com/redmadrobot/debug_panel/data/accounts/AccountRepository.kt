package com.redmadrobot.debug_panel.data.accounts

import com.redmadrobot.debug_panel.data.accounts.model.DebugUserCredentials
import io.reactivex.Completable
import io.reactivex.Single

interface AccountRepository {
    fun addCredential(credential: DebugUserCredentials): Completable
    fun getCredentials(): Single<List<DebugUserCredentials>>
    fun removeCredential(user: DebugUserCredentials): Completable
}
