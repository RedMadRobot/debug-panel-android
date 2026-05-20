package com.redmadrobot.debug.plugin.konfeature

import com.redmadrobot.konfeature.Konfeature

/**
 * No-op implementation of [KonfeaturePlugin] for release builds.
 *
 * Performs no actions; only mirrors the public constructor signature.
 */
@Suppress("UnusedPrivateProperty")
public class KonfeaturePlugin(
    private val debugPanelInterceptor: KonfeatureDebugPanelInterceptor,
    private val konfeature: Konfeature,
)
