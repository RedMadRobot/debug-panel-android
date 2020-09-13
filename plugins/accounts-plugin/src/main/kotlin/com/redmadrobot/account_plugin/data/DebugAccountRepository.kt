package com.redmadrobot.account_plugin.data

import com.redmadrobot.account_plugin.data.model.DebugAccount
import io.reactivex.Completable
import io.reactivex.Single

internal interface DebugAccountRepository {
    fun addAccount(account: DebugAccount): Completable

    fun updateAccount(account: DebugAccount): Completable

    fun getAccounts(): Single<List<DebugAccount>>

    fun getPreInstalledAccounts(): Single<List<DebugAccount>>

    fun removeAccount(user: DebugAccount): Completable
}
