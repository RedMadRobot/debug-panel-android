package com.redmadrobot.debug_panel.accounts.data.accounts

import com.redmadrobot.debug_panel.accounts.data.accounts.strategy.AccountsLoadStrategy
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import io.reactivex.Single

class AccountsProvider(private val accountsLoadStrategy: AccountsLoadStrategy) {

    fun getAccounts(): Single<List<DebugUserCredentials>> {
        return accountsLoadStrategy.loadAccounts()
    }
}
