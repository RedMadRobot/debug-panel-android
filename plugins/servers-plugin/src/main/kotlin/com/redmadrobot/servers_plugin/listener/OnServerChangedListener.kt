package com.redmadrobot.servers_plugin.listener

import com.redmadrobot.servers_plugin.data.model.DebugServer

interface OnServerChangedListener {
    fun onChanged(server: DebugServer?)
}
