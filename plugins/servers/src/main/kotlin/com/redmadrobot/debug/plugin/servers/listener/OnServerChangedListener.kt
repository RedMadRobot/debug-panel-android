package com.redmadrobot.debug.plugin.servers.listener

import com.redmadrobot.debug.plugin.servers.data.model.DebugServer

internal interface OnServerChangedListener {
    fun onChanged(server: DebugServer?)
}
