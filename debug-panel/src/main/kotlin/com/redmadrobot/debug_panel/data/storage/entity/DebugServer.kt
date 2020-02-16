package com.redmadrobot.debug_panel.data.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debug_server")
data class DebugServer(
    @PrimaryKey
    val url: String
)
