package com.redmadrobot.debug.plugin.servers.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class DebugServersData(
    val servers: List<DebugServer> = emptyList(),
    val selectedServer: DebugServer? = null
)
