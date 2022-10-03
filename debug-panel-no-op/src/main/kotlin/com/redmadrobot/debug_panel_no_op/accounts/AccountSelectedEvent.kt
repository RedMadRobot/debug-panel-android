package com.redmadrobot.debug.account.plugin

import com.redmadrobot.debug.account.data.model.DebugAccount
import com.redmadrobot.debug_panel_core.internal.DebugEvent

data class AccountSelectedEvent(val debugAccount: DebugAccount) : DebugEvent
