package com.redmadrobot.debug.plugin.aboutapp

import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppAction
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo

/**
 * No-op implementation of [AboutAppPlugin] for release builds.
 *
 * Performs no actions; only mirrors the public constructor signature.
 */
@Suppress("UnusedPrivateProperty")
public class AboutAppPlugin(
    private val appInfoList: List<AboutAppInfo>,
    private val actions: List<AboutAppAction> = emptyList(),
)
