package com.redmadrobot.debug_panel.data.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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
}
