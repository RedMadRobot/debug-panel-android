package com.redmadrobot.servers_plugin.plugin

import com.redmadrobot.debug.core.internal.DebugEvent
import com.redmadrobot.servers_plugin.data.model.DebugServer

public data class ServerSelectedEvent(val debugServer: DebugServer) : DebugEvent
