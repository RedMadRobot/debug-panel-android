package com.redmadrobot.account_plugin.data

import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.data.storage.DebugAccountDao
import com.redmadrobot.core.extension.subscribeOnIo
import io.reactivex.Completable
import io.reactivex.Single

internal class LocalDebugAccountRepository(
    private val debugAccountDao: DebugAccountDao,
    private val preInstalledAccounts: List<DebugAccount>
) : DebugAccountRepository {

    override fun addAccount(account: DebugAccount): Completable {
        return debugAccountDao.insert(account)
            .subscribeOnIo()
    }

    override fun updateAccount(account: DebugAccount): Completable {
        return debugAccountDao.update(account)
            .subscribeOnIo()
    }

    override fun getAccounts(): Single<List<DebugAccount>> {
        return debugAccountDao.getAll()
            .subscribeOnIo()
    }

    override fun getPreInstalledAccounts(): Single<List<DebugAccount>> {
        return Single.just(preInstalledAccounts).subscribeOnIo()
    }

    override fun removeAccount(user: DebugAccount): Completable {
        return debugAccountDao.remove(user)
            .subscribeOnIo()
    }
}
