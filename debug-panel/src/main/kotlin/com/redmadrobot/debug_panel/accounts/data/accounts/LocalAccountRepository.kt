package com.redmadrobot.debug_panel.accounts.data.accounts

import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentialsDao
import com.redmadrobot.debug_panel.extension.observeOnMain
import io.reactivex.Single

class LocalAccountRepository(private val debugUserCredentialsDao: DebugUserCredentialsDao) {

    fun getCredentials(): Single<List<DebugUserCredentials>> {
        return debugUserCredentialsDao.getAll()
            .observeOnMain()
    }
}
