package com.redmadrobot.debug.plugin.servers.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DebugStage.TABLE_NAME)
public data class DebugStage(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    override val name: String,
    val hosts: Map<String, String>,
    override val isDefault: Boolean = false
) : DebugServerData {

    internal companion object {
        const val TABLE_NAME = "debug_stage"
    }

    override fun equals(other: Any?): Boolean {
        val otherServer = other as DebugStage
        return this.hosts.size == otherServer.hosts.size && this.hosts == otherServer.hosts
    }
}
