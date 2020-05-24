package com.redmadrobot.core.data.accounts

import com.redmadrobot.core.data.PreInstalledData
import com.redmadrobot.core.data.storage.dao.DebugAccountDao
import com.redmadrobot.core.data.storage.entity.DebugAccount
import com.redmadrobot.core.extension.subscribeOnIo
import io.reactivex.Completable
import io.reactivex.Single

class LocalDebugAccountRepository(
    private val debugAccountDao: DebugAccountDao,
    private val preInstalledAccounts: PreInstalledData<DebugAccount>
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
        return Single.fromCallable {
            preInstalledAccounts.data
        }.subscribeOnIo()
    }

    override fun removeAccount(user: DebugAccount): Completable {
        return debugAccountDao.remove(user)
            .subscribeOnIo()
    }
}
