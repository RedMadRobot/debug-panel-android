package com.redmadrobot.debug.account.plugin

import com.redmadrobot.debug.account.data.model.DebugAccount
import com.redmadrobot.debug.core.internal.DebugEvent

public data class AccountSelectedEvent(val debugAccount: DebugAccount) : DebugEvent
