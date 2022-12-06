package com.redmadrobot.debug.noop.accounts

import com.redmadrobot.debug.account.data.model.DebugAccount
import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import java.util.Collections.emptyList

class AccountsPlugin(
    private val preInstalledAccounts: List<Any> = emptyList(),
    val debugAuthenticator: Any? = null
) {
    constructor(
        debugDataProvider: DebugDataProvider<List<DebugAccount>>,
        debugAuthenticator: Any? = null
    ) : this(
        preInstalledAccounts = debugDataProvider.provideData(),
        debugAuthenticator = debugAuthenticator
    )
}
