package com.redmadrobot.debug_panel.util

import com.redmadrobot.debug_panel.accounts.Authenticator

internal object DepContainerProvider {

    var depContainer: DepContainer? = null

    fun initContainer(authenticator: Authenticator?) {
        depContainer = DepContainer(authenticator)
    }
}
