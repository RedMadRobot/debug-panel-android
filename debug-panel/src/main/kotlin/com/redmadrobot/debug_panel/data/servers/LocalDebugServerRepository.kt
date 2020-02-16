package com.redmadrobot.debug_panel.data.servers

import com.redmadrobot.debug_panel.data.storage.dao.DebugServersDao
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class LocalDebugServerRepository(
    private val debugServersDao: DebugServersDao
) : DebugServerRepository {

    override fun addServer(server: DebugServer): Completable {
        return debugServersDao.insert(server)
            .subscribeOn(Schedulers.io())
    }

    override fun getAllServers(): Single<List<DebugServer>> {
        return debugServersDao.getAll()
            .subscribeOn(Schedulers.io())
    }

    override fun removeServer(server: DebugServer): Completable {
        return debugServersDao.remove(server)
            .subscribeOn(Schedulers.io())
    }
}
