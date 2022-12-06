package com.redmadrobot.debug.core

import android.content.Context
import com.redmadrobot.debug.core.annotation.DebugPanelInternal
import com.redmadrobot.debug.core.plugin.PluginDependencyContainer

@DebugPanelInternal
public class CommonContainer(public val context: Context) : PluginDependencyContainer
