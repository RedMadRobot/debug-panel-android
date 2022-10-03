package com.redmadrobot.debug.plugin.accounts.ui

import androidx.compose.runtime.Stable
import com.redmadrobot.account_plugin.ui.DebugAccountItem

@Stable
internal data class AccountsViewState(
    val preInstalledAccounts: List<DebugAccountItem> = emptyList(),
    val addedAccounts: List<DebugAccountItem> = emptyList(),
) {
    val allItems = preInstalledAccounts.plus(addedAccounts)
}
