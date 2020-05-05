package com.redmadrobot.debug_panel.data.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DebugAccountDao {
    @Query("SELECT * FROM debug_user_credentials")
    fun getAll(): Single<List<DebugAccount>>

    @Insert
    fun insert(user: DebugAccount): Completable

    @Delete
    fun remove(user: DebugAccount): Completable
}
