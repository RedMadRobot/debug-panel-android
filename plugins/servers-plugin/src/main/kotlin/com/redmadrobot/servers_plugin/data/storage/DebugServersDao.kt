package com.redmadrobot.servers_plugin.data.storage

import androidx.room.*
import com.redmadrobot.servers_plugin.data.model.DebugServer
import io.reactivex.Completable
import io.reactivex.Single

@Dao
internal interface DebugServersDao {

    @Query("SELECT * FROM ${DebugServer.TABLE_NAME}")
    fun getAll(): Single<List<DebugServer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(server: DebugServer): Completable

    @Delete
    fun remove(server: DebugServer): Completable

    @Update
    fun update(server: DebugServer): Completable

    @Query("SELECT * FROM ${DebugServer.TABLE_NAME} WHERE id = :serverId")
    fun getServer(serverId: Int): Single<DebugServer>
}
