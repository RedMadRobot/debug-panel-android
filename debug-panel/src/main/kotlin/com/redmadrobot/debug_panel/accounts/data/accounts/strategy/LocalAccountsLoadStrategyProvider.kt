package com.redmadrobot.debug_panel.accounts.data.accounts.strategy

import android.content.Context
import com.redmadrobot.debug_panel.accounts.data.storage.AppDatabase

internal class LocalAccountsLoadStrategyProvider(context: Context) {
    private val userDao = AppDatabase(context).getDebugUserCredentialsDao()

    fun getLocalAccountsLoadStrategy(): LocalAccountsLoadStrategy {
        return LocalAccountsLoadStrategy(userDao)
    }
}
