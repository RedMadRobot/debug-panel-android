package com.redmadrobot.debug_panel.data.servers.strategy

import com.redmadrobot.debug_panel.data.DebugDataLoadingStrategy
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import io.reactivex.Single

class PreinstalledServersLoadingStrategy : DebugDataLoadingStrategy<DebugServer> {

    override fun loadData(): Single<List<DebugServer>> {
        val preInstalledServers = listOf(
            DebugServer("https://testserver1.com")
        )

        return Single.just(preInstalledServers)
    }
}
