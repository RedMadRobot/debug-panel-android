package com.redmadrobot.debug_panel.data.accounts

import com.redmadrobot.debug_panel.data.accounts.model.DebugUserCredentials
import com.redmadrobot.debug_panel.data.accounts.strategy.AccountsLoadStrategy
import io.reactivex.Single

class AccountsProvider(private val accountsLoadStrategy: AccountsLoadStrategy) {

    fun getAccounts(): Single<List<DebugUserCredentials>> {
        return accountsLoadStrategy.loadAccounts()
    }
}
