package com.redmadrobot.servers_plugin.data.storage

import androidx.room.*
import com.redmadrobot.servers_plugin.data.model.DebugServer

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
    fun getServer(name: String, url: String): DebugServer?
}
