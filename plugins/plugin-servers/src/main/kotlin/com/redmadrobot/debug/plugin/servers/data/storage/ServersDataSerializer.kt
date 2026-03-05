package com.redmadrobot.debug.plugin.servers.data.storage

import androidx.datastore.core.Serializer
import com.redmadrobot.debug.plugin.servers.data.model.DebugServersData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalSerializationApi::class)
internal object ServersDataSerializer : Serializer<DebugServersData> {
    override val defaultValue: DebugServersData = DebugServersData()

    override suspend fun readFrom(input: InputStream): DebugServersData {
        return Json.decodeFromStream(input)
    }

    override suspend fun writeTo(t: DebugServersData, output: OutputStream) {
        Json.encodeToStream(t, output)
    }
}
