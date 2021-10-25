package com.redmadrobot.debug_panel_core

import android.content.Context
import com.redmadrobot.debug_panel_core.annotation.DebugPanelInternal
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer

@DebugPanelInternal
public class CommonContainer(public val context: Context) : PluginDependencyContainer
