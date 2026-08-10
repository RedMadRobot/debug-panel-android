package com.redmadrobot.debug.plugin.aboutapp.model

import java.util.UUID

/**
 * No-op declaration of [AboutAppInfo] for release builds.
 */
public data class AboutAppInfo(
    val title: String,
    val value: String,
) {
    val id: String = UUID.randomUUID().toString()
}
