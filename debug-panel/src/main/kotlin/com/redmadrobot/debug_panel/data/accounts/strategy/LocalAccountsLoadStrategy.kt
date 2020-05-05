package com.redmadrobot.debug_panel.data.accounts.strategy

import com.redmadrobot.debug_panel.data.DebugDataLoadingStrategy
import com.redmadrobot.debug_panel.data.accounts.AccountRepository
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import io.reactivex.Single

class LocalAccountsLoadStrategy(
    private val accountRepository: AccountRepository
) : DebugDataLoadingStrategy<DebugAccount> {

    override fun loadData(): Single<List<DebugAccount>> {
        return accountRepository.getCredentials()
    }
}
