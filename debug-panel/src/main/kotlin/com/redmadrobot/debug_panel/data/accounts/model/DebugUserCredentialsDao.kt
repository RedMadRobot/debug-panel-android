package com.redmadrobot.debug_panel.data.accounts.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface DebugUserCredentialsDao {
    @Query("SELECT * FROM debug_user_credentials")
    fun getAll(): Single<List<DebugUserCredentials>>

    @Insert
    fun insert(user: DebugUserCredentials): Completable

    @Delete
    fun remove(user: DebugUserCredentials): Completable
}
