package com.redmadrobot.debug.plugin.konfeature

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.plugin.konfeature.ui.KonfeatureScreen
import com.redmadrobot.konfeature.Konfeature

/**
 * Plugin for viewing and overriding feature flags from the Konfeature library.
 *
 * Allows changing feature flag values at runtime without rebuilding the app.
 * Overridden values are stored in SharedPreferences and survive app restarts.
 *
 * @param debugPanelInterceptor interceptor through which overrides are applied
 * @param konfeature Konfeature instance used to obtain the list of features
 *
 * @see KonfeatureDebugPanelInterceptor
 */
public class KonfeaturePlugin(
    private val debugPanelInterceptor: KonfeatureDebugPanelInterceptor,
    private val konfeature: Konfeature,
) : Plugin() {
    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return KonfeaturePluginContainer(konfeature, debugPanelInterceptor)
    }

    @Composable
    override fun content() {
        KonfeatureScreen()
    }

    private companion object {
        private const val NAME = "KONFEATURE"
    }
}
