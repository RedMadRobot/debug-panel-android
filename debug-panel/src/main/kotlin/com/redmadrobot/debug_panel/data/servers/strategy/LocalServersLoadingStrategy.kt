package com.redmadrobot.debug_panel.data.servers.strategy

import com.redmadrobot.debug_panel.data.DebugDataLoadingStrategy
import com.redmadrobot.debug_panel.data.servers.DebugServerRepository
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import io.reactivex.Single

class LocalServersLoadingStrategy(
    private val debugServerRepository: DebugServerRepository
) : DebugDataLoadingStrategy<DebugServer> {

    override fun loadData(): Single<List<DebugServer>> {
        return debugServerRepository.getAllServers()
    }
}
