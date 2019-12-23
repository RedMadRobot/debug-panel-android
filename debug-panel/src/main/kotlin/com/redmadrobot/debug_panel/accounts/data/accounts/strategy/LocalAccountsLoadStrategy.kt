package com.redmadrobot.debug_panel.accounts.data.accounts.strategy

import android.content.Context
import com.redmadrobot.debug_panel.accounts.data.accounts.LocalAccountRepository
import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials

class LocalAccountsLoadStrategy(context: Context) : AccountsLoadStrategy {
    val accountRepository: LocalAccountRepository = LocalAccountRepository(context)

    override fun loadAccounts(): List<DebugUserCredentials> {
        //TODO Реализовать загрузку аккаунтов из локального хранилища через LocalAccountRepository
        throw NotImplementedError()
    }
}
