package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer

public data class ServerSelectedEvent(val debugServer: DebugServer) : DebugEvent
