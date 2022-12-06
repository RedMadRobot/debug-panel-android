package com.redmadrobot.debug.accounts.ui

import com.redmadrobot.account_plugin.ui.item.DebugAccountItems

internal data class AccountsViewState(
    val preInstalledAccounts: List<DebugAccountItems>,
    val addedAccounts: List<DebugAccountItems>
)
