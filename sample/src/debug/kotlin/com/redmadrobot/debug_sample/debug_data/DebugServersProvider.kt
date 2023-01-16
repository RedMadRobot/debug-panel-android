package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.servers.data.model.DebugServer
import com.redmadrobot.debug.servers.data.model.DebugServerData
import com.redmadrobot.debug.servers.data.model.DebugStage

class DebugServersProvider {

    fun provideData(): List<DebugServerData> {
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
            DebugStage(
                name = "debug stage 1",
                hosts = mapOf(
                    "main" to "https://testserver1main.com",
                    "s3" to "https://testserver1s3.com",
                    "wss" to "https://testserver1wss.com"
                ),
                isDefault = true
            ),
            DebugStage(
                name = "debug stage 2",
                hosts = mapOf(
                    "main" to "https://testserver2main.com",
                    "s3" to "https://testserver2s3.com",
                    "wss" to "https://testserver2wss.com"
                )
            ),
        )
    }
}
