package com.redmadrobot.debug.accounts.plugin

import com.redmadrobot.debug.accounts.data.model.DebugAccount
import com.redmadrobot.debug.core.internal.DebugEvent

public data class AccountSelectedEvent(val debugAccount: DebugAccount) : DebugEvent