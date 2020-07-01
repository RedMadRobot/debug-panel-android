package com.redmadrobot.core.data.storage.dao

import androidx.room.*
import com.redmadrobot.core.data.storage.entity.DebugAccount
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DebugAccountDao {
    @Query("SELECT * FROM ${DebugAccount.TABLE_NAME}")
    fun getAll(): Single<List<DebugAccount>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: DebugAccount): Completable

    @Delete
    fun remove(user: DebugAccount): Completable

    @Update
    fun update(user: DebugAccount): Completable

}
