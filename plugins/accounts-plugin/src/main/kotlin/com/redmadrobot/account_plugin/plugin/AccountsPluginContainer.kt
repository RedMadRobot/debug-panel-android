package com.redmadrobot.account_plugin.plugin

import com.redmadrobot.account_plugin.data.LocalDebugAccountRepository
import com.redmadrobot.account_plugin.ui.AccountsViewModel
import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.data.PreInstalledData
import com.redmadrobot.core.data.storage.entity.DebugAccount
import com.redmadrobot.core.plugin.PluginDependencyContainer

internal class AccountsPluginContainer(
    private val preInstalledAccounts: PreInstalledData<DebugAccount>,
    private val container: CommonContainer
) : PluginDependencyContainer {

    private val debugAccountRepository by lazy {
        LocalDebugAccountRepository(
            container.dataBaseInstance.getDebugAccountsDao(),
            preInstalledAccounts
        )
    }

    fun createAccountsViewModel(): AccountsViewModel {
        return AccountsViewModel(container.context, debugAccountRepository)
    }
}
