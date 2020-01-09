package com.redmadrobot.debug_panel.accounts.data.accounts.strategy

import com.redmadrobot.debug_panel.accounts.data.accounts.LocalAccountRepository
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import io.reactivex.Single

class LocalAccountsLoadStrategy(private val accountRepository: LocalAccountRepository) :
    AccountsLoadStrategy {

    override fun loadAccounts(): Single<List<DebugUserCredentials>> {
        return accountRepository.getCredentials()
    }
}
