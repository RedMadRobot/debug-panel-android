package com.redmadrobot.debug_panel.accounts.data.accounts.strategy

import com.redmadrobot.debug_panel.accounts.data.model.DebugUserCredentials

interface AccountsLoadStrategy {
    fun loadAccounts(): List<DebugUserCredentials>
}
