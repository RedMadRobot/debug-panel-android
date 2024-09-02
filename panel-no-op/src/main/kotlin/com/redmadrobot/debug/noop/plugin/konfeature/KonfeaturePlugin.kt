package com.redmadrobot.debug.plugin.konfeature

import com.redmadrobot.konfeature.Konfeature

public class KonfeaturePlugin(
    private val debugPanelInterceptor: KonfeatureDebugPanelInterceptor,
    private val konfeature: Konfeature,
)
