package com.redmadrobot.debug.servers.plugin

import com.redmadrobot.debug.core.internal.DebugEvent
import com.redmadrobot.debug.servers.data.model.DebugServer

public data class ServerSelectedEvent(val debugServer: DebugServer) : DebugEvent
