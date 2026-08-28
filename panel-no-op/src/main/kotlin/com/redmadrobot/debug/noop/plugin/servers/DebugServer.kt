package com.redmadrobot.debug.plugin.servers.data.model

import dev.drewhamilton.poko.Poko

/**
 * No-op declaration of [DebugServer] for release builds.
 */
@Poko
public class DebugServer(
    public val name: String,
    public val url: String,
    public val isDefault: Boolean = false
)
