package com.redmadrobot.debug.account.plugin

import com.redmadrobot.debug.account.data.LocalDebugAccountRepository
import com.redmadrobot.debug.account.data.model.DebugAccount
import com.redmadrobot.debug.account.data.storage.AccountsPluginDatabase
import com.redmadrobot.debug.account.ui.AccountsViewModel
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer

internal class AccountsPluginContainer(
    private val preInstalledAccounts: List<DebugAccount>,
    private val container: CommonContainer
) : PluginDependencyContainer {


    private val pluginStorage by lazy { AccountsPluginDatabase.getInstance(container.context) }

    private val debugAccountRepository by lazy {
        LocalDebugAccountRepository(
            pluginStorage.getDebugAccountsDao(),
            preInstalledAccounts
        )
    }

    fun createAccountsViewModel(): AccountsViewModel {
        return AccountsViewModel(container.context, debugAccountRepository)
    }
}
