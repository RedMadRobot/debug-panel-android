package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.plugin.servers.data.model.DebugServer

class DebugServersProvider {
    fun provideData(): List<DebugServer> {
        return listOf(
            DebugServer(
                name = "debug 1",
                url = "https://testserver1.com",
                isDefault = true
            ),
            DebugServer(
                name = "debug 2", url = "https://testserver2.com"
            ),
            DebugServer(
                name = "debug 3", url = "https://testserver3.com"
            ),
            DebugServer(
                name = "debug 4", url = "https://testserver4.com"
            ),
        )
    }
}
