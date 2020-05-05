package com.redmadrobot.debug_panel.data.accounts

import com.redmadrobot.debug_panel.data.storage.dao.DebugAccountDao
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import com.redmadrobot.debug_panel.extension.subscribeOnIo
import io.reactivex.Completable
import io.reactivex.Single

class LocalAccountRepository(private val debugAccountDao: DebugAccountDao) :
    AccountRepository {

    override fun addCredential(credential: DebugAccount): Completable {
        return debugAccountDao.insert(credential)
            .subscribeOnIo()
    }

    override fun getCredentials(): Single<List<DebugAccount>> {
        return debugAccountDao.getAll()
            .subscribeOnIo()
    }

    override fun removeCredential(user: DebugAccount): Completable {
        return debugAccountDao.remove(user)
            .subscribeOnIo()
    }
}
