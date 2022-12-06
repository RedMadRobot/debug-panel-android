package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.servers.data.model.DebugServer

class DebugServersProvider : DebugDataProvider<List<DebugServer>> {

    override fun provideData(): List<DebugServer> {
        return listOf(
            DebugServer(name = "debug 1", url = "https://testserver1.com", isDefault = true),
            DebugServer(name = "debug 2", url = "https://testserver2.com")
        )
    }
}
