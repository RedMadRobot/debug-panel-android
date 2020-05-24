package com.redmadrobot.core.data.servers

import com.redmadrobot.core.data.PreInstalledData
import com.redmadrobot.core.data.storage.dao.DebugServersDao
import com.redmadrobot.core.data.storage.entity.DebugServer
import com.redmadrobot.core.extension.subscribeOnIo
import io.reactivex.Completable
import io.reactivex.Single

class LocalDebugServerRepository(
    private val debugServersDao: DebugServersDao,
    private val preInstalledServers: PreInstalledData<DebugServer>
) : DebugServerRepository {

    override fun addServer(server: DebugServer): Completable {
        return debugServersDao.insert(server)
            .subscribeOnIo()
    }

    override fun getPreInstalledServers(): Single<List<DebugServer>> {
        return Single.fromCallable {
            preInstalledServers.data
        }
    }

    override fun getServers(): Single<List<DebugServer>> {
        return debugServersDao.getAll()
            .subscribeOnIo()
    }

    override fun removeServer(server: DebugServer): Completable {
        return debugServersDao.remove(server)
            .subscribeOnIo()
    }

    override fun updateServer(server: DebugServer): Completable {
        return debugServersDao.update(server)
            .subscribeOnIo()
    }

    override fun setSelected(id: Int): Completable {
        return clearSelection()
            .andThen(debugServersDao.getServer(id))
            .flatMapCompletable { server ->
                val updatedServerData = server.copy(isSelected = true)
                updateServer(updatedServerData)
            }
    }

    override fun clearSelection(): Completable {
        return debugServersDao.getSelectedServer()
            .onErrorReturnItem(DebugServer.getEmpty())
            .flatMapCompletable { server ->
                if (server.url.isNotEmpty()) {
                    val updatedServerData = server.copy(isSelected = false)
                    updateServer(updatedServerData)
                } else {
                    Completable.complete()
                }
            }
    }
}
