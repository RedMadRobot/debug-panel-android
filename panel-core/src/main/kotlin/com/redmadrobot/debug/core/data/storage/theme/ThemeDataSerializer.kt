package com.redmadrobot.debug.core.data.storage.theme

import androidx.datastore.core.Serializer
import com.redmadrobot.debug.core.data.storage.model.ThemeData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalSerializationApi::class)
internal object ThemeDataSerializer : Serializer<ThemeData> {
    override val defaultValue: ThemeData = ThemeData()

    override suspend fun readFrom(input: InputStream): ThemeData {
        return Json.decodeFromStream(input)
    }

    override suspend fun writeTo(themeData: ThemeData, output: OutputStream) {
        Json.encodeToStream(themeData, output)
    }
}
