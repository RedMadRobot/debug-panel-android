package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.flipper.plugin.PluginToggle

internal class DebugFlipperFeaturesProvider : DebugDataProvider<List<PluginToggle>> {
    override fun provideData(): List<PluginToggle> {
        return emptyList()
    }
}
