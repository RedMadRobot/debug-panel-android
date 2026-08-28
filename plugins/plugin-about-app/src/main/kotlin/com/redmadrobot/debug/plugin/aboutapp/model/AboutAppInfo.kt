package com.redmadrobot.debug.plugin.aboutapp.model

import dev.drewhamilton.poko.Poko
import java.util.UUID

/**
 * Information entry for [com.redmadrobot.debug.plugin.aboutapp.AboutAppPlugin].
 *
 * @property title parameter name (e.g., "Version", "Build type")
 * @property value parameter value (e.g., "1.2.3", "debug")
 */
@Poko
public class AboutAppInfo(
    public val title: String,
    public val value: String,
) {
    internal val id: String = UUID.randomUUID().toString()
}
