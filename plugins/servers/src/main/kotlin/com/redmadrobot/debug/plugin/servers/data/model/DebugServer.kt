package com.redmadrobot.debug.plugin.servers.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DebugServer.TABLE_NAME)
public data class DebugServer(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    override val name: String,
    val url: String,
    override val isDefault: Boolean = false
) : DebugServerData {
    internal companion object {
        const val TABLE_NAME = "debug_server"
    }

    override fun equals(other: Any?): Boolean {
        val otherServer = other as DebugServer
        return this.name == otherServer.name && this.url == otherServer.url
    }
}
