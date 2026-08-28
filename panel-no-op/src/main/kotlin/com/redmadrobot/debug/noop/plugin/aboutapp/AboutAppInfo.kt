package com.redmadrobot.debug.plugin.aboutapp.model

import dev.drewhamilton.poko.Poko
import java.util.UUID

/**
 * No-op declaration of [AboutAppInfo] for release builds.
 */
@Poko
public class AboutAppInfo(
    public val title: String,
    public val value: String,
) {
    internal val id: String = UUID.randomUUID().toString()
}
