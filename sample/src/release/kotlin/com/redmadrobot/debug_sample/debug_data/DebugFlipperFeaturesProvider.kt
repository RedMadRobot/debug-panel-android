package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.flipper.config.FlipperValue

internal class DebugFlipperFeaturesProvider : DebugDataProvider<Map<String, FlipperValue>> {
    override fun provideData(): Map<String, FlipperValue> {
        return emptyMap()
    }
}
