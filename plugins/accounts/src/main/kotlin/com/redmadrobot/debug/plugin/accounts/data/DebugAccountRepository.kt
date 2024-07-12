package com.redmadrobot.debug.plugin.accounts.data

import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount

internal interface DebugAccountRepository {
    suspend fun addAccount(account: DebugAccount)

    suspend fun updateAccount(account: DebugAccount)

    suspend fun getAccounts(): List<DebugAccount>

    suspend fun getPreInstalledAccounts(): List<DebugAccount>

    suspend fun removeAccount(user: DebugAccount)
}
