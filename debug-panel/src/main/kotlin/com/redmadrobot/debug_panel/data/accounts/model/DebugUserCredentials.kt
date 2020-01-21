package com.redmadrobot.debug_panel.data.accounts.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debug_user_credentials")
data class DebugUserCredentials(
    @PrimaryKey
    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "password")
    val password: String
) {
    companion object {
        fun empty() = DebugUserCredentials("", "")
    }
}
