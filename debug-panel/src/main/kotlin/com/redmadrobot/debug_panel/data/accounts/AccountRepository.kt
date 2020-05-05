package com.redmadrobot.debug_panel.data.accounts

import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import io.reactivex.Completable
import io.reactivex.Single

interface AccountRepository {
    fun addCredential(credential: DebugAccount): Completable
    fun getCredentials(): Single<List<DebugAccount>>
    fun removeCredential(user: DebugAccount): Completable
}
