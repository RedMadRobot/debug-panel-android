package com.redmadrobot.debug.plugin.servers.data.model

import kotlinx.serialization.Serializable

/**
 * Server (environment) model for [com.redmadrobot.debug.plugin.servers.ServersPlugin].
 *
 * @property name display name of the server
 * @property url base URL of the server (e.g., `https://api.example.com`)
 * @property isDefault `true` if the server is used by default.
 */
@Serializable
public data class DebugServer(
    val name: String,
    val url: String,
    val isDefault: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        val otherServer = other as DebugServer
        return this.name == otherServer.name && this.url == otherServer.url
    }
}
