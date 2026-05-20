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
public interface PluginDependencyContainer
