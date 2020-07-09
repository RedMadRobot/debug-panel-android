package com.redmadrobot.account_plugin.plugin

import com.redmadrobot.account_plugin.data.LocalDebugAccountRepository
import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.account_plugin.data.storage.AccountsPluginDatabase
import com.redmadrobot.account_plugin.ui.AccountsViewModel
import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.plugin.PluginDependencyContainer

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
