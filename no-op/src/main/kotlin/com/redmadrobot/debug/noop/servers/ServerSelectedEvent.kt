package com.redmadrobot.debug.servers.plugin

import com.redmadrobot.debug.core.internal.DebugEvent
import com.redmadrobot.debug.servers.data.model.DebugServer

data class ServerSelectedEvent(val debugServer: DebugServer) : DebugEvent
