package com.redmadrobot.debug.account.plugin

import com.redmadrobot.debug.account.data.model.DebugAccount
import com.redmadrobot.debug.core.data.DebugDataProvider
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
