package com.redmadrobot.debug_panel.data.accounts

import com.redmadrobot.debug_panel.data.DebugDataLoadingStrategy
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import io.reactivex.Single

class AccountsProvider(private val dataLoadStrategy: DebugDataLoadingStrategy<DebugAccount>) {

    fun getAccounts(): Single<List<DebugAccount>> {
        return dataLoadStrategy.loadData()
    }
}
