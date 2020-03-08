package com.redmadrobot.debug_panel.data.storage.dao

import androidx.room.*
import com.redmadrobot.debug_panel.data.storage.entity.DebugServer
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DebugServersDao {

    @Query("SELECT * FROM debug_server")
    fun getAll(): Single<List<DebugServer>>

    @Insert
    fun insert(server: DebugServer): Completable

    @Delete
    fun remove(server: DebugServer): Completable

    @Update
    fun update(server: DebugServer): Completable

}
