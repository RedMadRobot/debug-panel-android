package com.redmadrobot.debug_panel.data.accounts.strategy

import com.redmadrobot.debug_panel.data.DebugDataLoadingStrategy
import com.redmadrobot.debug_panel.data.storage.entity.DebugUserCredentials
import io.reactivex.Single

class PreinstalledAccountsLoadStrategy : DebugDataLoadingStrategy<DebugUserCredentials> {

    override fun loadData(): Single<List<DebugUserCredentials>> {
        return listOf(
            DebugUserCredentials(
                "7882340482",
                "Qq!11111"
            ),
            DebugUserCredentials(
                "2777248041",
                "Qq!11111"
            ),
            DebugUserCredentials(
                "4183730054",
                "Ww!11111"
            ),
            DebugUserCredentials(
                "1944647499",
                "Qq!11111"
            )
        ).let { Single.just(it) }
    }
}
