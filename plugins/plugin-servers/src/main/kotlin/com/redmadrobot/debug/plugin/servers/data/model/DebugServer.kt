package com.redmadrobot.debug.plugin.servers.data.model

import kotlinx.serialization.Serializable

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
