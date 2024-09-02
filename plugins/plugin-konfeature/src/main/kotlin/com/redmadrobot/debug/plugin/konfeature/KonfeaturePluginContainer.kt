package com.redmadrobot.debug.plugin.konfeature

import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.konfeature.ui.KonfeatureViewModel
import com.redmadrobot.konfeature.Konfeature

internal class KonfeaturePluginContainer(
    private val konfeature: Konfeature,
    private val debugPanelInterceptor: KonfeatureDebugPanelInterceptor,
) : PluginDependencyContainer {

    fun createKonfeatureViewModel(): KonfeatureViewModel {
        return KonfeatureViewModel(konfeature, debugPanelInterceptor)
    }
}
