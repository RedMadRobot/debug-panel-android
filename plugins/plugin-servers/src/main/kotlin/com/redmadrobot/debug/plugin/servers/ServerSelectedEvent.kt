package com.redmadrobot.debug.plugin.servers

import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer

/**
 * Event emitted when a server is selected in [ServersPlugin].
 *
 * Subscribe via [com.redmadrobot.debug.core.DebugPanel.observeEvents] to react to environment changes.
 *
 * @property debugServer the selected server
 */
public data class ServerSelectedEvent(val debugServer: DebugServer) : DebugEvent
