package com.redmadrobot.debug_panel.accounts.data.accounts.strategy

import android.content.Context
import com.redmadrobot.debug_panel.accounts.data.accounts.AccountRepository
import com.redmadrobot.debug_panel.accounts.data.accounts.LocalAccountRepository
import com.redmadrobot.debug_panel.accounts.data.storage.AppDatabase

internal class AccountRepositoryProvider(context: Context) {
    private val userDao = AppDatabase.getInstance(context).getDebugUserCredentialsDao()

    fun getAccountRepository(): AccountRepository {
        return LocalAccountRepository(userDao)
    }
}
