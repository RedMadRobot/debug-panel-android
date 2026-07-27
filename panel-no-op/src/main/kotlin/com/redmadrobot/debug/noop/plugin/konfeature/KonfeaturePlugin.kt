package com.redmadrobot.debug.plugin.konfeature

import com.redmadrobot.konfeature.Konfeature
import com.redmadrobot.konfeature.ui.KonfeatureDebugStore

/**
 * No-op implementation of [KonfeaturePlugin] for release builds.
 *
 * Performs no actions; only mirrors the public constructor signature. The overrides store and
 * interceptor come from `konfeature-ui-noop` in release builds.
 */
@Suppress("UnusedPrivateProperty")
public class KonfeaturePlugin(
    private val konfeature: Konfeature,
    private val store: KonfeatureDebugStore,
)
