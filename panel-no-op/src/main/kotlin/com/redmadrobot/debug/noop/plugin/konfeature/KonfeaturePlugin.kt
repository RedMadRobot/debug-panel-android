package com.redmadrobot.debug.plugin.konfeature

import com.redmadrobot.konfeature.Konfeature

/**
 * No-op implementation of [KonfeaturePlugin] for release builds.
 *
 * Performs no actions; only mirrors the public constructor signature. The overrides store and
 * interceptor come from `konfeature-ui-noop` in release builds via [KonfeatureDebugPanelConfig].
 */
@Suppress("UnusedPrivateProperty")
public class KonfeaturePlugin(
    private val konfeature: Konfeature,
    private val config: KonfeatureDebugPanelConfig,
)
