package com.redmadrobot.account_plugin.data

import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.data.storage.DebugAccountDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class LocalDebugAccountRepository(
    private val debugAccountDao: DebugAccountDao,
    private val preInstalledAccounts: List<DebugAccount>
) : DebugAccountRepository {

    override suspend fun addAccount(account: DebugAccount) {
        withContext(Dispatchers.IO) {
            debugAccountDao.insert(account)
        }
    }

    override suspend fun updateAccount(account: DebugAccount) {
        withContext(Dispatchers.IO) {
            debugAccountDao.update(account)
        }
    }

    override suspend fun getAccounts(): List<DebugAccount> {
        return withContext(Dispatchers.IO) {
            debugAccountDao.getAll()
        }
    }

    override suspend fun getPreInstalledAccounts(): List<DebugAccount> {
        return preInstalledAccounts
    }

    override suspend fun removeAccount(user: DebugAccount) {
        return withContext(Dispatchers.IO) {
            debugAccountDao.remove(user)
        }
    }
}
