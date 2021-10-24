package com.redmadrobot.servers_plugin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DebugServer.TABLE_NAME)
public data class DebugServer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val url: String,
    val isDefault: Boolean = false
) {
    internal companion object {
        const val TABLE_NAME = "debug_server"
    }

    override fun equals(other: Any?): Boolean {
        return with(other as DebugServer) {
            this.name == name && this.url == url
        }
    }

}
