package com.redmadrobot.core.authenticator

import com.redmadrobot.core.data.storage.entity.DebugAccount

interface Authenticator {
    fun authenticate(account: DebugAccount)
}
