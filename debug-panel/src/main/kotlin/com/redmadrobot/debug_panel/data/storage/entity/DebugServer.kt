package com.redmadrobot.debug_panel.data.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DebugServer.TABLE_NAME)
data class DebugServer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String,
    val isSelected: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "debug_server"

        fun getEmpty() = DebugServer(url = "")
    }
}
