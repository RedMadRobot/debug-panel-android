package com.redmadrobot.debug_panel.data.accounts.strategy

import com.redmadrobot.debug_panel.data.accounts.AccountRepository
import com.redmadrobot.debug_panel.data.accounts.model.DebugUserCredentials
import io.reactivex.Single

class LocalAccountsLoadStrategy(private val accountRepository: AccountRepository) :
    AccountsLoadStrategy {

    override fun loadAccounts(): Single<List<DebugUserCredentials>> {
        return accountRepository.getCredentials()
    }
}
