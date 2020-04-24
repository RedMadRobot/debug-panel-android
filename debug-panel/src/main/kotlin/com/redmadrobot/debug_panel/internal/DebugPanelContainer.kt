package com.redmadrobot.debug_panel.internal

import android.content.Context
import com.redmadrobot.debug_panel.data.accounts.AccountRepository
import com.redmadrobot.debug_panel.data.accounts.AccountsProvider
import com.redmadrobot.debug_panel.data.accounts.strategy.AccountRepositoryProvider
import com.redmadrobot.debug_panel.data.accounts.strategy.LocalAccountsLoadStrategy
import com.redmadrobot.debug_panel.data.accounts.strategy.PreinstalledAccountsLoadStrategy
import com.redmadrobot.debug_panel.data.servers.DebugServerRepository
import com.redmadrobot.debug_panel.data.servers.LocalDebugServerRepository
import com.redmadrobot.debug_panel.data.storage.AppDatabase
import com.redmadrobot.debug_panel.data.storage.PreferenceRepository
import com.redmadrobot.debug_panel.data.toggles.LocalFeatureToggleRepository
import com.redmadrobot.debug_panel.inapp.toggles.FeatureToggleHolder
import com.redmadrobot.debug_panel.ui.accounts.AccountsViewModel
import com.redmadrobot.debug_panel.ui.servers.ServersViewModel
import com.redmadrobot.debug_panel.ui.toggles.FeatureTogglesViewModel

class DebugPanelContainer(
    context: Context,
    debugPanelConfig: DebugPanelConfig
) {

    internal val dataBaseInstance: AppDatabase

    /*Accounts region*/
    internal val accountRepository: AccountRepository
    internal val localAccountProvider: AccountsProvider
    internal val preInstalledAccountProvider: AccountsProvider
    internal val featureToggleHolder: FeatureToggleHolder
    /*endregion*/

    /*Servers region*/
    internal val serversRepository: DebugServerRepository
    /*endregion*/

    /*Feature toggle region*/
    internal val preferenceRepository: PreferenceRepository
    internal val localFeatureToggleRepository: LocalFeatureToggleRepository
    /*endregion*/

    init {
        this.dataBaseInstance = AppDatabase.getInstance(context)

        //Accounts
        this.accountRepository = AccountRepositoryProvider(context).getAccountRepository()
        this.localAccountProvider = AccountsProvider(LocalAccountsLoadStrategy(accountRepository))
        this.preInstalledAccountProvider = AccountsProvider(PreinstalledAccountsLoadStrategy())
        //

        //Servers
        this.serversRepository = LocalDebugServerRepository(
            dataBaseInstance.getDebugServersDao(),
            debugPanelConfig.preInstalledServers
        )
        //

        //Feature toggle
        this.preferenceRepository = PreferenceRepository(context)
        this.localFeatureToggleRepository = LocalFeatureToggleRepository(
            dataBaseInstance.getFeatureTogglesDao(),
            PreferenceRepository(context)
        )
        this.featureToggleHolder = FeatureToggleHolder(this.localFeatureToggleRepository)
        //
    }

    fun createAccountsViewModel(): AccountsViewModel {
        return AccountsViewModel(
            accountRepository,
            localAccountProvider,
            preInstalledAccountProvider
        )
    }

    fun createServersViewModel(): ServersViewModel {
        return ServersViewModel(serversRepository)
    }

    fun createFeatureTogglesViewModel(): FeatureTogglesViewModel {
        return FeatureTogglesViewModel(localFeatureToggleRepository, preferenceRepository)
    }
}
