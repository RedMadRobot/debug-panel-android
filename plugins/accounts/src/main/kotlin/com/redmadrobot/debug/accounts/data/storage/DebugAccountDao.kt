package com.redmadrobot.debug.accounts.data.storage

import androidx.room.*
import com.redmadrobot.debug.accounts.data.model.DebugAccount

@Dao
internal interface DebugAccountDao {

    @Query("SELECT * FROM ${DebugAccount.TABLE_NAME}")
    suspend fun getAll(): List<DebugAccount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: DebugAccount)

    @Delete
    suspend fun remove(account: DebugAccount)

    @Update
    suspend fun update(account: DebugAccount)
}
