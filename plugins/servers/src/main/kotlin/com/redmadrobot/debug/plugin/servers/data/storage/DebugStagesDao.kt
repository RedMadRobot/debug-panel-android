package com.redmadrobot.debug.plugin.servers.data.storage

import androidx.room.*
import com.redmadrobot.debug.plugin.servers.data.model.DebugStage

@Dao
internal interface DebugStagesDao {

    @Query("SELECT * FROM ${DebugStage.TABLE_NAME}")
    suspend fun getAll(): List<DebugStage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(server: DebugStage)

    @Delete
    suspend fun remove(server: DebugStage)

    @Update
    suspend fun update(server: DebugStage)

    @Query("SELECT * FROM ${DebugStage.TABLE_NAME} WHERE name = :name")
    fun getStage(name: String): DebugStage?
}
