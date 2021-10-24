package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.servers_plugin.data.model.DebugServer

class DebugServersProvider : DebugDataProvider<List<DebugServer>> {

    override fun provideData(): List<DebugServer> {
        return listOf(
            DebugServer(name = "debug 1", url = "https://testserver1.com", isDefault = true)
        )
    }
}
