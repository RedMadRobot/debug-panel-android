package com.redmadrobot.debug.accounts.ui

import androidx.compose.runtime.Stable

@Stable
internal data class AccountsViewState(
    val preInstalledAccounts: List<DebugAccountItem> = emptyList(),
    val addedAccounts: List<DebugAccountItem> = emptyList(),
) {
    val allItems = preInstalledAccounts.plus(addedAccounts)
}
