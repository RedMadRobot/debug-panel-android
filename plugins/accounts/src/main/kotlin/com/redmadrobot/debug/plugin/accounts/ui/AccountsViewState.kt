package com.redmadrobot.debug.plugin.accounts.ui

import com.redmadrobot.debug.plugin.accounts.ui.item.DebugAccountItems

internal data class AccountsViewState(
    val preInstalledAccounts: List<DebugAccountItems>,
    val addedAccounts: List<DebugAccountItems>
)
