package com.redmadrobot.debug.plugin.servers.data.storage

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


internal class DbConverters {

    @TypeConverter
    fun fromMap(map: Map<String, String>?): String {
        return Json.encodeToString(map ?: emptyMap())
    }

    @TypeConverter
    fun toMap(data: String?): Map<String, String> {
        return data?.let { Json.decodeFromString(data) } ?: emptyMap()
    }
}