package com.redmadrobot.debug.account.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DebugAccount.TABLE_NAME)
public data class DebugAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "pin")
    val pin: String = ""
) {
    internal companion object {
        const val TABLE_NAME: String = "debug_account"
    }
}
