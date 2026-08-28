package com.redmadrobot.debug.plugin.servers.data.model

import dev.drewhamilton.poko.Poko
import kotlinx.serialization.Serializable

/**
 * Server (environment) model for [com.redmadrobot.debug.plugin.servers.ServersPlugin].
 *
 * @property name display name of the server
 * @property url base URL of the server (e.g., `https://api.example.com`)
 * @property isDefault `true` if the server is used by default.
 */
@Serializable
@Poko
public class DebugServer(
    public val name: String,
    public val url: String,
    public val isDefault: Boolean = false
)
