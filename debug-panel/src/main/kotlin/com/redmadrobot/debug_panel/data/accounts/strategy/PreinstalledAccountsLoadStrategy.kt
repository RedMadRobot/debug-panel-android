package com.redmadrobot.debug_panel.data.accounts.strategy

import com.redmadrobot.debug_panel.data.DebugDataLoadingStrategy
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import io.reactivex.Single

class PreinstalledAccountsLoadStrategy : DebugDataLoadingStrategy<DebugAccount> {

    override fun loadData(): Single<List<DebugAccount>> {
        return listOf(
            DebugAccount(
                "7882340482",
                "Qq!11111"
            ),
            DebugAccount(
                "2777248041",
                "Qq!11111"
            ),
            DebugAccount(
                "4183730054",
                "Ww!11111"
            ),
            DebugAccount(
                "1944647499",
                "Qq!11111"
            )
        ).let { Single.just(it) }
    }
}
