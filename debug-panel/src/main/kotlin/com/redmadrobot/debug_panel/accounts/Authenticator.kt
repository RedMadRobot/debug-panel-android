package com.redmadrobot.debug_panel.accounts

import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount

interface Authenticator {
    fun authenticate(account: DebugAccount)
}
