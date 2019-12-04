package com.redmadrobot.debug_panel.accounts.data.model

data class DebugUserCredentials(
        val login: String,
        val password: String
) {
    companion object {
        fun empty() = DebugUserCredentials("", "")
    }
}
