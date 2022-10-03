package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.account.data.model.DebugAccount
import com.redmadrobot.debug_panel_core.data.DebugDataProvider

class DebugAccountsProvider : DebugDataProvider<List<DebugAccount>> {

    override fun provideData(): List<DebugAccount> {
        return listOf(
            DebugAccount(
                login = "7882340482",
                password = "Qq!11111"
            ),
            DebugAccount(
                login = "2777248041",
                password = "Qq!11111"
            ),
            DebugAccount(
                login = "4183730054",
                password = "Ww!11111",
                pin = "112"
            ),
            DebugAccount(
                login = "1944647499",
                password = "Qq!11111",
                pin = "1111"
            )
        )
    }
}
