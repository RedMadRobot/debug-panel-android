package com.redmadrobot.debug_panel.data.accounts

import com.redmadrobot.debug_panel.data.storage.dao.DebugUserCredentialsDao
import com.redmadrobot.debug_panel.data.storage.entity.DebugUserCredentials
import com.redmadrobot.debug_panel.extension.subscribeOnIo
import io.reactivex.Completable
import io.reactivex.Single

class LocalAccountRepository(private val debugUserCredentialsDao: DebugUserCredentialsDao) :
    AccountRepository {

    override fun addCredential(credential: DebugUserCredentials): Completable {
        return debugUserCredentialsDao.insert(credential)
            .subscribeOnIo()
    }

    override fun getCredentials(): Single<List<DebugUserCredentials>> {
        return debugUserCredentialsDao.getAll()
            .subscribeOnIo()
    }

    override fun removeCredential(user: DebugUserCredentials): Completable {
        return debugUserCredentialsDao.remove(user)
            .subscribeOnIo()
    }
}
