package com.redmadrobot.debug.core.internal

import android.content.Context
import com.redmadrobot.debug.core.annotation.DebugPanelInternal

/**
 * Container with shared dependencies, available to all plugins.
 *
 * Passed into [com.redmadrobot.debug.core.plugin.Plugin.getPluginContainer]
 * when a plugin is initialized.
 *
 * @property context application context
 */
@DebugPanelInternal
public class CommonContainer(public val context: Context) : PluginDependencyContainer
