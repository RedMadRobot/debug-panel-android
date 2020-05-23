package com.redmadrobot.debug_panel.internal.plugin.account

import com.redmadrobot.debug_panel.data.PreInstalledData
import com.redmadrobot.debug_panel.data.accounts.LocalDebugAccountRepository
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import com.redmadrobot.debug_panel.internal.CommonContainer
import com.redmadrobot.debug_panel.internal.plugin.PluginDependencyContainer
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
