package com.redmadrobot.debug_panel.accounts.data.accounts

import com.redmadrobot.debug_panel.accounts.data.accounts.strategy.AccountsLoadStrategy
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials

class AccountsProvider(private val accountsLoadStrategy: AccountsLoadStrategy) {

    fun getAccounts(): List<DebugUserCredentials> {
        return accountsLoadStrategy.loadAccounts()
    }
}
