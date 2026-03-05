package com.redmadrobot.debug.plugin.servers.data.storage

import android.content.Context
import androidx.datastore.dataStore

internal val Context.serversStorage by dataStore(
    fileName = "plugin_servers.json",
    serializer = ServersDataSerializer
)
