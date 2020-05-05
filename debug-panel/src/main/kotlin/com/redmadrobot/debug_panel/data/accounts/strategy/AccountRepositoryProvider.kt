package com.redmadrobot.debug_panel.data.accounts.strategy

import android.content.Context
import com.redmadrobot.debug_panel.data.accounts.DebugAccountRepository
import com.redmadrobot.debug_panel.data.accounts.LocalDebugAccountRepository
import com.redmadrobot.debug_panel.data.storage.AppDatabase

internal class AccountRepositoryProvider(context: Context) {
    private val userDao = AppDatabase.getInstance(context).getDebugAccountsDao()

    fun getAccountRepository(): DebugAccountRepository {
        return LocalDebugAccountRepository(userDao)
    }
}
