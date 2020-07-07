package com.redmadrobot.servers_plugin.listener

import com.redmadrobot.core.data.storage.entity.DebugServer

interface OnServerChangedListener {
    fun onChanged(server: DebugServer?)
}
