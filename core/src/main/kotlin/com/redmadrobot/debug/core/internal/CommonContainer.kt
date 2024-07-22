package com.redmadrobot.debug.core.internal

import android.content.Context
import com.redmadrobot.debug.core.annotation.DebugPanelInternal

@DebugPanelInternal
public class CommonContainer(public val context: Context) : PluginDependencyContainer
