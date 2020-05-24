package com.redmadrobot.debug_panel.accounts

import com.redmadrobot.core.data.storage.entity.DebugAccount

interface Authenticator {
    fun authenticate(account: DebugAccount)
}
