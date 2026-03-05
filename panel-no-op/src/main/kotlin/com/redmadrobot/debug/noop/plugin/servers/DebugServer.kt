package com.redmadrobot.debug.plugin.servers.data.model

data class DebugServer(
    val name: String,
    val url: String,
    val isDefault: Boolean = false
)
