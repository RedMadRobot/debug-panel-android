package com.redmadrobot.debug_panel.data.accounts.strategy

import com.redmadrobot.debug_panel.data.DebugDataLoadingStrategy
import com.redmadrobot.debug_panel.data.accounts.AccountRepository
import com.redmadrobot.debug_panel.data.accounts.model.DebugUserCredentials
import io.reactivex.Single

class LocalAccountsLoadStrategy(
    private val accountRepository: AccountRepository
) : DebugDataLoadingStrategy<DebugUserCredentials> {

    override fun loadData(): Single<List<DebugUserCredentials>> {
        return accountRepository.getCredentials()
    }
}
