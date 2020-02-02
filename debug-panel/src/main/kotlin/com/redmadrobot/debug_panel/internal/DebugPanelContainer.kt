package com.redmadrobot.debug_panel.internal

import android.content.Context
import com.redmadrobot.debug_panel.data.accounts.AccountRepository
import com.redmadrobot.debug_panel.data.accounts.AccountsProvider
import com.redmadrobot.debug_panel.data.accounts.strategy.AccountRepositoryProvider
import com.redmadrobot.debug_panel.data.accounts.strategy.LocalAccountsLoadStrategy
import com.redmadrobot.debug_panel.data.accounts.strategy.PreinstalledAccountsLoadStrategy
import com.redmadrobot.debug_panel.ui.accounts.AccountsViewModel
import com.redmadrobot.debug_panel.ui.toggles.FeatureTogglesViewModel

class DebugPanelContainer(context: Context) {

    internal var accountRepository: AccountRepository
    internal var localAccountProvider: AccountsProvider
    internal var preInstalledAccountProvider: AccountsProvider

    init {
        this.accountRepository = AccountRepositoryProvider(context).getAccountRepository()
        this.localAccountProvider = AccountsProvider(LocalAccountsLoadStrategy(accountRepository))
        this.preInstalledAccountProvider = AccountsProvider(PreinstalledAccountsLoadStrategy())
    }

    fun createAccountsViewModel(): AccountsViewModel {
        return AccountsViewModel(
            accountRepository,
            localAccountProvider,
            preInstalledAccountProvider
        )
    }

    fun createFeatureTogglesViewModel(): FeatureTogglesViewModel {
        return FeatureTogglesViewModel()
    }
}
