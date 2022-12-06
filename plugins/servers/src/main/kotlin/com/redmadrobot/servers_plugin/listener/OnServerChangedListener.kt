package com.redmadrobot.servers_plugin.listener

import com.redmadrobot.servers_plugin.data.model.DebugServer

internal interface OnServerChangedListener {
    fun onChanged(server: DebugServer?)
}
