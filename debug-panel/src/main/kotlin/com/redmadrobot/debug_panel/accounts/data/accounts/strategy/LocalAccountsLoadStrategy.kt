package com.redmadrobot.debug_panel.accounts.data.accounts.strategy

import com.redmadrobot.debug_panel.accounts.data.accounts.LocalAccountRepository
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentialsDao
import io.reactivex.Single

class LocalAccountsLoadStrategy(debugUserCredentialsDao: DebugUserCredentialsDao) :
    AccountsLoadStrategy {
    private val accountRepository: LocalAccountRepository =
        LocalAccountRepository(debugUserCredentialsDao)

    override fun loadAccounts(): Single<List<DebugUserCredentials>> {
        return accountRepository.getCredentials()
    }
}
