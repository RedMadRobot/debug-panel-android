package com.redmadrobot.debug_panel.internal

import android.content.Context
import com.redmadrobot.debug_panel.data.accounts.AccountRepository
import com.redmadrobot.debug_panel.data.accounts.AccountsProvider
import com.redmadrobot.debug_panel.data.accounts.strategy.AccountRepositoryProvider
import com.redmadrobot.debug_panel.data.accounts.strategy.LocalAccountsLoadStrategy
import com.redmadrobot.debug_panel.data.accounts.strategy.PreinstalledAccountsLoadStrategy
import com.redmadrobot.debug_panel.data.servers.DebugServerRepository
import com.redmadrobot.debug_panel.data.servers.DebugServersProvider
import com.redmadrobot.debug_panel.data.servers.LocalDebuServerRepository
import com.redmadrobot.debug_panel.data.servers.strategy.LocalServersLoadingStrategy
import com.redmadrobot.debug_panel.data.servers.strategy.PreinstalledServersLoadingStrategy
import com.redmadrobot.debug_panel.data.storage.AppDatabase
import com.redmadrobot.debug_panel.ui.accounts.AccountsViewModel

class DebugPanelContainer(context: Context) {

    internal val dataBaseInstance: AppDatabase

    /*Accounts region*/
    internal var accountRepository: AccountRepository
    internal var localAccountProvider: AccountsProvider
    internal var preInstalledAccountProvider: AccountsProvider
    /*endregion*/

    /*Servers region*/
    internal var serversRepository: DebugServerRepository
    internal var localServersProvider: DebugServersProvider
    internal var preInstalledServersProvider: DebugServersProvider
    /*endregion*/

    init {
        this.dataBaseInstance = AppDatabase.getInstance(context)

        //Accounts
        this.accountRepository = AccountRepositoryProvider(context).getAccountRepository()
        this.localAccountProvider = AccountsProvider(LocalAccountsLoadStrategy(accountRepository))
        this.preInstalledAccountProvider = AccountsProvider(PreinstalledAccountsLoadStrategy())
        //

        //Servers
        this.serversRepository = LocalDebuServerRepository(dataBaseInstance.getDebugServersDao())
        this.localServersProvider = DebugServersProvider(
            LocalServersLoadingStrategy(serversRepository)
        )
        this.preInstalledServersProvider = DebugServersProvider(
            PreinstalledServersLoadingStrategy()
        )
        //
    }

    fun createAccountsViewModel(): AccountsViewModel {
        return AccountsViewModel(
            accountRepository,
            localAccountProvider,
            preInstalledAccountProvider
        )
    }
}
