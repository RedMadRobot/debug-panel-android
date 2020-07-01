package com.redmadrobot.core.data.storage.dao

import androidx.room.*
import com.redmadrobot.core.data.storage.entity.DebugServer
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DebugServersDao {

    @Query("SELECT * FROM ${DebugServer.TABLE_NAME}")
    fun getAll(): Single<List<DebugServer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(server: DebugServer): Completable

    @Delete
    fun remove(server: DebugServer): Completable

    @Update
    fun update(server: DebugServer): Completable

    @Query("SELECT * FROM ${DebugServer.TABLE_NAME} WHERE isSelected = 1")
    fun getSelectedServer(): Single<DebugServer>

    @Query("SELECT * FROM ${DebugServer.TABLE_NAME} WHERE id = :serverId")
    fun getServer(serverId: Int): Single<DebugServer>
}
