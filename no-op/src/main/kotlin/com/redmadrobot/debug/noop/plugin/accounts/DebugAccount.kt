package com.redmadrobot.debug.plugin.accounts.data.model

data class DebugAccount(
    val id: Int = 0,
    val login: String,
    val password: String,
    val pin: String = ""
)
