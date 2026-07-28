package com.redmadrobot.debug.core.internal

import com.redmadrobot.debug.core.annotation.DebugPanelInternal

/**
 * Marker interface for plugin dependency containers.
 *
 * Each plugin implements its own container, extending this interface, to hold repositories, interactors, and other dependencies.
 *
 * @see com.redmadrobot.debug.core.plugin.Plugin.getPluginContainer
 * @see CommonContainer
 */
@DebugPanelInternal
public interface PluginDependencyContainer {
    /**
     * Shared no-op container for plugins that have no dependencies of their own. Return it from
     * [com.redmadrobot.debug.core.plugin.Plugin.getPluginContainer] instead of allocating an empty
     * anonymous implementation.
     */
    public object Empty : PluginDependencyContainer
}
