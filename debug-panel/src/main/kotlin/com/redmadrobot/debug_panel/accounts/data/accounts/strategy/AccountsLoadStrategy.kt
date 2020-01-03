package com.redmadrobot.debug_panel.accounts.data.accounts.strategy

import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials
import io.reactivex.Single

interface AccountsLoadStrategy {
    fun loadAccounts(): Single<List<DebugUserCredentials>>
}
