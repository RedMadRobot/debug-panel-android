package com.redmadrobot.debug_panel.data.servers

import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import io.reactivex.Completable
import io.reactivex.Single

interface DebugServerRepository {
    fun addServer(server: DebugServer): Completable
    fun getAllServers(): Single<List<DebugServer>>
    fun removeServer(server: DebugServer): Completable
}
