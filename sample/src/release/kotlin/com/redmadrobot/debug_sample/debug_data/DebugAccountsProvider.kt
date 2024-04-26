package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.accounts.data.model.DebugAccount
import com.redmadrobot.debug.core.data.DebugDataProvider

class DebugAccountsProvider : DebugDataProvider<List<DebugAccount>> {

    override fun provideData(): List<DebugAccount> {
        return emptyList()
    }
}
