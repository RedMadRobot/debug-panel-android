package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.plugin.PluginToggle

internal class DebugFlipperFeaturesProvider : DebugDataProvider<List<PluginToggle>> {
    override fun provideData(): List<PluginToggle> {
        return emptyList()
    }
}
