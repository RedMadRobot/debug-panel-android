package com.redmadrobot.debug.plugin.aboutapp

/**
 * No-op implementation of [AboutAppPlugin] for release builds.
 *
 * Performs no actions; only mirrors the public constructor signature.
 */
@Suppress("UnusedPrivateProperty")
public class AboutAppPlugin(
    private val appInfoList: List<AboutAppInfo>
)
