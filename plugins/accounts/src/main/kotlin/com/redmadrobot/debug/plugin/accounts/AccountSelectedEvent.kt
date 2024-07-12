package com.redmadrobot.debug.plugin.accounts

import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount
import com.redmadrobot.debug.core.internal.DebugEvent

public data class AccountSelectedEvent(val debugAccount: DebugAccount) : DebugEvent
