package com.redmadrobot.debug.plugin.aboutapp.model

import java.util.UUID

/**
 * Information entry for [com.redmadrobot.debug.plugin.aboutapp.AboutAppPlugin].
 *
 * @property title parameter name (e.g., "Version", "Build type")
 * @property value parameter value (e.g., "1.2.3", "debug")
 */
public data class AboutAppInfo(
    val title: String,
    val value: String,
) {
    val id: String = UUID.randomUUID().toString()
}
