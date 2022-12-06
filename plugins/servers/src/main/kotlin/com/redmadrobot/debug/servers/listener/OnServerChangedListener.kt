package com.redmadrobot.debug.servers.listener

import com.redmadrobot.debug.servers.data.model.DebugServer

internal interface OnServerChangedListener {
    fun onChanged(server: DebugServer?)
}
