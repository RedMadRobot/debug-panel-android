package com.redmadrobot.servers_plugin.data.storage

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DebugServersDao {

    @Query("SELECT * FROM ${com.redmadrobot.servers_plugin.data.model.DebugServer.TABLE_NAME}")
    fun getAll(): Single<List<com.redmadrobot.servers_plugin.data.model.DebugServer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(server: com.redmadrobot.servers_plugin.data.model.DebugServer): Completable

    @Delete
    fun remove(server: com.redmadrobot.servers_plugin.data.model.DebugServer): Completable

    @Update
    fun update(server: com.redmadrobot.servers_plugin.data.model.DebugServer): Completable

    @Query("SELECT * FROM ${com.redmadrobot.servers_plugin.data.model.DebugServer.TABLE_NAME} WHERE id = :serverId")
    fun getServer(serverId: Int): Single<com.redmadrobot.servers_plugin.data.model.DebugServer>
}
