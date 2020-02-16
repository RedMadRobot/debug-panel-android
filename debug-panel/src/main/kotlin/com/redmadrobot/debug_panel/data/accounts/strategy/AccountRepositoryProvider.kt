package com.redmadrobot.debug_panel.data.accounts.strategy

import android.content.Context
import com.redmadrobot.debug_panel.data.accounts.AccountRepository
import com.redmadrobot.debug_panel.data.accounts.LocalAccountRepository
import com.redmadrobot.debug_panel.data.storage.AppDatabase

internal class AccountRepositoryProvider(context: Context) {
    private val userDao = AppDatabase.getInstance(context).getDebugUserCredentialsDao()

    fun getAccountRepository(): AccountRepository {
        return LocalAccountRepository(userDao)
    }
}
