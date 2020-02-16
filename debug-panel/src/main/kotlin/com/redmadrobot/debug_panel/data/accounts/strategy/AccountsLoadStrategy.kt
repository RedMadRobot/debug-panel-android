package com.redmadrobot.debug_panel.data.accounts.strategy

import com.redmadrobot.debug_panel.data.accounts.model.DebugUserCredentials
import io.reactivex.Single

interface AccountsLoadStrategy {
    fun loadAccounts(): Single<List<DebugUserCredentials>>
}
