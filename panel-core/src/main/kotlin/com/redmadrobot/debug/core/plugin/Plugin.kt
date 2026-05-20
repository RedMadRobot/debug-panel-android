package com.redmadrobot.debug.core.plugin

import androidx.compose.runtime.Composable
import com.redmadrobot.debug.core.DebugEvent
import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.internal.PluginDependencyContainer

/**
 * Base class for all debug panel plugins.
 *
 * Each plugin is an isolated module with its own UI and dependencies.
 * To create a plugin, you need to:
 * 1. Extend [Plugin]
 * 2. Implement [getName] and [getPluginContainer]
 * 3. Override [content] to render the UI
 *
 * @see DebugPanel.initialize
 * @see PluginDependencyContainer
 */
public abstract class Plugin {
    private lateinit var pluginContainer: PluginDependencyContainer

    internal fun start(commonContainer: CommonContainer): Plugin {
        pluginContainer = getPluginContainer(commonContainer)
        return this
    }

    /**
     * Sends an event to the debug panel's shared event bus.
     *
     * Subscribers will receive the event via [DebugPanel.subscribeToEvents] or [DebugPanel.observeEvents].
     *
     * @param debugEvent event to send
     */
    public fun pushEvent(debugEvent: DebugEvent) {
        DebugPanel.getInstance()?.pushEvent(debugEvent)
    }

    /**
     * Returns the plugin's dependency container, cast to type [T].
     *
     * @throws ClassCastException if the container does not match the requested type
     */
    public fun <T> getContainer(): T = pluginContainer as T

    /**
     * Composable function that renders the plugin's main UI in the debug panel.
     */
    @Suppress("TopLevelComposableFunctions", "ComposableFunctionName")
    @Composable
    public open fun content() {
    }

    /**
     * Creates and returns the plugin's dependency container.
     *
     * Called during plugin initialization. Use [commonContainer] to access shared dependencies (e.g., [android.content.Context]).
     *
     * @param commonContainer container with the panel's shared dependencies
     * @return the dependency container for this plugin
     */
    public abstract fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer

    /**
     * Returns the plugin's unique name.
     *
     * Used to identify the plugin in the registry.
     */
    public abstract fun getName(): String
}
