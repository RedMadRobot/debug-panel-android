package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import dev.drewhamilton.poko.Poko

/**
 * No-op declaration of [ServerSelectedEvent] for release builds.
 */
@Poko
public class ServerSelectedEvent(public val debugServer: DebugServer) : DebugEvent
