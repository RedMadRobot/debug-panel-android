package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount

class DebugAccountsProvider : DebugDataProvider<List<DebugAccount>> {

    override fun provideData(): List<DebugAccount> {
        return emptyList()
    }
}
