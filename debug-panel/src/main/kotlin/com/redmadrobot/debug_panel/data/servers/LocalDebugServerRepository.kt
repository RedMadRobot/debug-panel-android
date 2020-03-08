package com.redmadrobot.debug_panel.data.servers

import com.redmadrobot.debug_panel.data.storage.dao.DebugServersDao
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import com.redmadrobot.debug_panel.extension.subscribeOnIo
import io.reactivex.Completable
import io.reactivex.Single

class LocalDebugServerRepository(
    private val debugServersDao: DebugServersDao
) : DebugServerRepository {

    override fun addServer(server: DebugServer): Completable {
        return debugServersDao.insert(server)
            .subscribeOnIo()
    }

    override fun getAllServers(): Single<List<DebugServer>> {
        return debugServersDao.getAll()
            .subscribeOnIo()
    }

    override fun removeServer(server: DebugServer): Completable {
        return debugServersDao.remove(server)
            .subscribeOnIo()
    }

    override fun updateServer(server: DebugServer): Completable {
        return debugServersDao.update(server)
    }
}
