package com.redmadrobot.debug.plugin.accounts

import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.accounts.data.LocalDebugAccountRepository
import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount
import com.redmadrobot.debug.plugin.accounts.data.storage.AccountsPluginDatabase
import com.redmadrobot.debug.plugin.accounts.ui.AccountsViewModel

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
