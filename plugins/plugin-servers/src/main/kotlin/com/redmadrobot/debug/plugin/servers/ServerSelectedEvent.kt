package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import dev.drewhamilton.poko.Poko

/**
 * Event emitted when a server is selected in [ServersPlugin].
 *
 * Subscribe via [com.redmadrobot.debug.core.DebugPanel.observeEvents] to react to environment changes.
 *
 * @property debugServer the selected server
 */
@Poko
public class ServerSelectedEvent(public val debugServer: DebugServer) : DebugEvent
