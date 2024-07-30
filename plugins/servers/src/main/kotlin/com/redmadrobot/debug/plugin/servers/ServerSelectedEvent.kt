package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import com.redmadrobot.debug.plugin.servers.data.model.DebugStage

public data class ServerSelectedEvent(val debugServer: DebugServer) : DebugEvent

public data class StageSelectedEvent(val debugServer: DebugStage) : DebugEvent
