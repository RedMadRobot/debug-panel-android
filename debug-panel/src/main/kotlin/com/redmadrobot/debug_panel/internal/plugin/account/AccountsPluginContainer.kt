package com.redmadrobot.debug_panel.internal.plugin.account

import com.redmadrobot.core.CommonContainer
import com.redmadrobot.core.PluginDependencyContainer
import com.redmadrobot.core.data.PreInstalledData
import com.redmadrobot.core.data.accounts.LocalDebugAccountRepository
import com.redmadrobot.core.data.storage.entity.DebugAccount
import com.redmadrobot.debug_panel.ui.accounts.AccountsViewModel

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
