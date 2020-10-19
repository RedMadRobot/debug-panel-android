package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.debug_panel_core.data.DebugDataProvider

class DebugAccountsProvider : DebugDataProvider<List<DebugAccount>> {

    override fun provideData(): List<DebugAccount> {
        return emptyList()
    }
}
