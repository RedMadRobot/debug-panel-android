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
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DebugStage

        if (name != other.name) return false
        if (hosts != other.hosts) return false
        if (isDefault != other.isDefault) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + hosts.hashCode()
        result = 31 * result + isDefault.hashCode()
        return result
    }
}
