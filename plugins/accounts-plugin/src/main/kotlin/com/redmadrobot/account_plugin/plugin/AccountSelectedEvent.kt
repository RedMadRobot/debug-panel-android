package com.redmadrobot.account_plugin.plugin

import com.redmadrobot.account_plugin.data.model.DebugAccount
import com.redmadrobot.core.internal.DebugEvent

data class AccountSelectedEvent(val debugAccount: DebugAccount) : DebugEvent
