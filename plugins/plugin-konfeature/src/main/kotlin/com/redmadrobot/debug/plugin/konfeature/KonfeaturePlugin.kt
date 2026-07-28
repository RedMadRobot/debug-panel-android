package com.redmadrobot.debug.plugin.konfeature

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.uikit.theme.DebugPanelTheme
import com.redmadrobot.konfeature.Konfeature
import com.redmadrobot.konfeature.ui.KonfeatureDebugPanel
import com.redmadrobot.konfeature.ui.presentation.theme.KonfeatureTheme

/**
 * Plugin for viewing and overriding feature flags from the Konfeature library.
 *
 * The screen, feature list and persistence are provided by the `konfeature-ui` library. This
 * plugin only embeds [KonfeatureDebugPanel] into the debug panel using the library's default
 * palette, and keeps it in sync with the debug panel's light/dark theme mode. Boolean values are
 * toggled inline by the panel; other value types are shown read-only.
 *
 * Overridden values are persisted by [config]'s store (DataStore) and survive app restarts.
 *
 * Pass the same [config] you attached to [konfeature] via
 * [applyDebugPanelConfig]; the constructor fails fast if it was never attached, since an unattached
 * config means the panel would show values it can never override.
 *
 * @param konfeature Konfeature instance whose feature configs are displayed
 * @param config the config created via [KonfeatureDebugPanelConfig.create] and attached to
 *   [konfeature] with [applyDebugPanelConfig]
 *
 * @see KonfeatureDebugPanelConfig
 */
public class KonfeaturePlugin(
    private val konfeature: Konfeature,
    private val config: KonfeatureDebugPanelConfig,
) : Plugin() {
    init {
        check(config.isAttached) {
            "KonfeatureDebugPanelConfig was not attached to a Konfeature instance. " +
                "Call konfeature { applyDebugPanelConfig(config) } before passing the config to " +
                "KonfeaturePlugin."
        }
    }

    override fun getName(): String = NAME

    /**
     * The plugin has no dependencies of its own — the screen, state and persistence are provided by
     * the `konfeature-ui` library — so an empty container is returned.
     */
    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return PluginDependencyContainer.Empty
    }

    @Composable
    override fun content() {
        KonfeatureTheme(isDarkTheme = DebugPanelTheme.isDarkTheme) {
            KonfeatureDebugPanel(
                konfeature = konfeature,
                store = config.store,
            )
        }
    }

    private companion object {
        private const val NAME = "KONFEATURE"
    }
}
