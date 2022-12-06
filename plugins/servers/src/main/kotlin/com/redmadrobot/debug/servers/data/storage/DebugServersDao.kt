package com.redmadrobot.debug.servers.data.storage

import androidx.room.*
import com.redmadrobot.debug.servers.data.model.DebugServer

@Dao
internal interface DebugServersDao {

    @Query("SELECT * FROM ${DebugServer.TABLE_NAME}")
    suspend fun getAll(): List<DebugServer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(server: DebugServer)

    @Delete
    suspend fun remove(server: DebugServer)

    @Update
    suspend fun update(server: DebugServer)

    @Query("SELECT * FROM ${DebugServer.TABLE_NAME} WHERE name = :name AND url =:url")
    suspend fun getServer(name: String, url: String): DebugServer?
}
