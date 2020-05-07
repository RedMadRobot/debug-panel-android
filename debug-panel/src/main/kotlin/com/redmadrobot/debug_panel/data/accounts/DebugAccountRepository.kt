package com.redmadrobot.debug_panel.data.accounts

import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import io.reactivex.Completable
import io.reactivex.Single

interface DebugAccountRepository {
    fun addAccount(account: DebugAccount): Completable

    fun updateAccount(account: DebugAccount): Completable

    fun getAccounts(): Single<List<DebugAccount>>

    fun getPreInstalledAccounts(): Single<List<DebugAccount>>

    fun removeAccount(user: DebugAccount): Completable
}
