package com.redmadrobot.debug.accounts.plugin

import com.redmadrobot.debug.accounts.data.LocalDebugAccountRepository
import com.redmadrobot.debug.accounts.data.model.DebugAccount
import com.redmadrobot.debug.accounts.data.storage.AccountsPluginDatabase
import com.redmadrobot.debug.accounts.ui.AccountsViewModel
import com.redmadrobot.debug.core.CommonContainer
import com.redmadrobot.debug.core.plugin.PluginDependencyContainer

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
