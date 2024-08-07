package com.redmadrobot.debug.plugin.konfeature

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.plugin.konfeaure.ui.KonfeatureScreen
import com.redmadrobot.konfeature.Konfeature

public class KonfeaturePlugin(
    private val debugPanelInterceptor: KonfeatureDebugPanelInterceptor,
    private val konfeature: Konfeature,
) : Plugin() {

    public companion object {
        private const val NAME = "KONFEATURE"
    }

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return KonfeaturePluginContainer(konfeature, debugPanelInterceptor)
    }

    @Composable
    override fun content() {
        KonfeatureScreen()
    }
}
