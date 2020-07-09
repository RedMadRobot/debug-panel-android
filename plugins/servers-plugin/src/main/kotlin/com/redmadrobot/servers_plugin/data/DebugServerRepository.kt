package com.redmadrobot.servers_plugin.data

import com.redmadrobot.servers_plugin.data.model.DebugServer
import io.reactivex.Completable
import io.reactivex.Single

interface DebugServerRepository {
    fun addServer(server: DebugServer): Completable
    fun getPreInstalledServers(): Single<List<DebugServer>>
    fun getServers(): Single<List<DebugServer>>
    fun removeServer(server: DebugServer): Completable
    fun updateServer(server: DebugServer): Completable
    fun setSelected(id: Int): Completable
    fun clearSelection(): Completable
}
