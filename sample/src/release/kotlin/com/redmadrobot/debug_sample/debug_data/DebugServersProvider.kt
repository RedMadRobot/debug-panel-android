package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.servers.data.model.DebugServer

class DebugServersProvider : DebugDataProvider<List<DebugServer>> {
    override fun provideData(): List<DebugServer> {
        return emptyList()
    }
}
