package com.redmadrobot.account_plugin.ui

import com.redmadrobot.account_plugin.ui.item.AccountItems

internal data class AccountsViewState(
    val preInstalledAccounts: List<AccountItems>,
    val addedAccounts: List<AccountItems>
)
