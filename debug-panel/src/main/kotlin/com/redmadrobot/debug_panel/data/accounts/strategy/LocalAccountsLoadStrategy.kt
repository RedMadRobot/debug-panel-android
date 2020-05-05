package com.redmadrobot.debug_panel.data.accounts.strategy

import com.redmadrobot.debug_panel.data.DebugDataLoadingStrategy
import com.redmadrobot.debug_panel.data.accounts.DebugAccountRepository
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import io.reactivex.Single

class LocalAccountsLoadStrategy(
    private val debugAccountRepository: DebugAccountRepository
) : DebugDataLoadingStrategy<DebugAccount> {

    override fun loadData(): Single<List<DebugAccount>> {
        return debugAccountRepository.getCredentials()
    }
}
