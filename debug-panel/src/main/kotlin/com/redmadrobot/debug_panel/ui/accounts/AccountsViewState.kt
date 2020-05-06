package com.redmadrobot.debug_panel.ui.accounts

import com.xwray.groupie.kotlinandroidextensions.Item

data class AccountsViewState(
    val preInstalledItems: List<Item>,
    val addedItems: List<Item>
)
