package com.redmadrobot.debug.plugin.konfeature

import com.redmadrobot.konfeature.Konfeature

@Suppress("UnusedPrivateProperty")
public class KonfeaturePlugin(
    private val debugPanelInterceptor: KonfeatureDebugPanelInterceptor,
    private val konfeature: Konfeature,
)
