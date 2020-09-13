package com.redmadrobot.account_plugin.ui

import com.xwray.groupie.kotlinandroidextensions.Item

internal data class AccountsViewState(
    val preInstalledItems: List<Item>,
    val addedItems: List<Item>
)
