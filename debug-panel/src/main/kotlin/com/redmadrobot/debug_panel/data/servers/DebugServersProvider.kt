package com.redmadrobot.debug_panel.data.servers

import com.redmadrobot.debug_panel.data.DebugDataLoadingStrategy
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import io.reactivex.Single

class DebugServersProvider(private val dataLoadingStrategy: DebugDataLoadingStrategy<DebugServer>) {

    fun loadServers(): Single<List<DebugServer>> {
        return dataLoadingStrategy.loadData()
    }
}
