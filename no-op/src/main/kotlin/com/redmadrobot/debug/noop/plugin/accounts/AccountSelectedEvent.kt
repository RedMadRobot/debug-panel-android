package com.redmadrobot.debug.plugin.accounts

import com.redmadrobot.debug.core.internal.DebugEvent
import com.redmadrobot.debug.plugin.accounts.data.model.DebugAccount

data class AccountSelectedEvent(val debugAccount: DebugAccount) : DebugEvent
