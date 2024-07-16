package com.redmadrobot.debug.servers.plugin

import com.redmadrobot.debug.core.internal.DebugEvent
import com.redmadrobot.debug.servers.data.model.DebugServer
import com.redmadrobot.debug.servers.data.model.DebugStage

public data class ServerSelectedEvent(val debugServer: DebugServer) : DebugEvent

public data class StageSelectedEvent(val debugServer: DebugStage) : DebugEvent
