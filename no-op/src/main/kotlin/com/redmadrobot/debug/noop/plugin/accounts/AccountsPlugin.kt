package com.redmadrobot.debug.plugin.accounts

import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount
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
