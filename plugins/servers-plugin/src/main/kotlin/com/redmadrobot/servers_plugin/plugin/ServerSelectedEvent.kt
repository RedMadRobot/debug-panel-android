package com.redmadrobot.servers_plugin.plugin

import com.redmadrobot.core.internal.DebugEvent
import com.redmadrobot.servers_plugin.data.model.DebugServer

data class ServerSelectedEvent(val debugServer: DebugServer) : DebugEvent
