package com.redmadrobot.debug.plugin.servers.data.model

/**
 * No-op declaration of [DebugServer] for release builds.
 */
public data class DebugServer(
    val name: String,
    val url: String,
    val isDefault: Boolean = false
)
