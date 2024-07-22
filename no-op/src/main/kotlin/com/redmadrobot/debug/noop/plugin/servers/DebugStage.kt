package com.redmadrobot.debug.plugin.servers.data.model


data class DebugStage(
    val id: Int = 0,
    val name: String,
    val hosts: Map<String, String>,
    val isDefault: Boolean = false
)
