package com.redmadrobot.debug_panel.data.storage.dao

import androidx.room.*
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class DebugServersDao {

    @Query("SELECT * FROM ${DebugServer.TABLE_NAME}")
    abstract fun getAll(): Single<List<DebugServer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(server: DebugServer): Completable

    @Delete
    abstract fun remove(server: DebugServer): Completable

    @Update
    abstract fun update(server: DebugServer): Completable

    @Query("SELECT * FROM ${DebugServer.TABLE_NAME} WHERE isSelected = 1")
    abstract fun getSelectedServer(): Single<DebugServer>

    @Query("SELECT * FROM ${DebugServer.TABLE_NAME} WHERE id = :serverId")
    abstract fun getServer(serverId: Int): Single<DebugServer>
}
