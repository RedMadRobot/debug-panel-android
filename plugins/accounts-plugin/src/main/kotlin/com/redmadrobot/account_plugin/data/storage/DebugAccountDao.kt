package com.redmadrobot.account_plugin.data.storage

import androidx.room.*
import com.redmadrobot.account_plugin.data.model.DebugAccount
import io.reactivex.Completable
import io.reactivex.Single

@Dao
internal interface DebugAccountDao {
    @Query("SELECT * FROM ${DebugAccount.TABLE_NAME}")
    fun getAll(): Single<List<DebugAccount>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: DebugAccount): Completable

    @Delete
    fun remove(user: DebugAccount): Completable

    @Update
    fun update(user: DebugAccount): Completable

}
