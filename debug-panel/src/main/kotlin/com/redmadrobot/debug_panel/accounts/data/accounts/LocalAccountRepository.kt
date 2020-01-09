package com.redmadrobot.debug_panel.accounts.data.accounts

import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentialsDao
import com.redmadrobot.debug_panel.extension.subscribeOnIo
import io.reactivex.Completable
import io.reactivex.Single

class LocalAccountRepository(private val debugUserCredentialsDao: DebugUserCredentialsDao) {

    fun addCredential(credential: DebugUserCredentials): Completable {
        return debugUserCredentialsDao.insert(credential)
            .subscribeOnIo()
    }

    fun getCredentials(): Single<List<DebugUserCredentials>> {
        return debugUserCredentialsDao.getAll()
            .subscribeOnIo()
    }
}
