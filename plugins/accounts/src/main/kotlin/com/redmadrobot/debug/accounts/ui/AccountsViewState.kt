package com.redmadrobot.debug.accounts.ui

import com.redmadrobot.debug.accounts.ui.item.DebugAccountItems

internal data class AccountsViewState(
    val preInstalledAccounts: List<DebugAccountItems>,
    val addedAccounts: List<DebugAccountItems>
)
