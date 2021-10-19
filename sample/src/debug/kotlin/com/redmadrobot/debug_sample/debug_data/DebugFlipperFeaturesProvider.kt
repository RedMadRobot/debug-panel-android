package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.flipper.config.FlipperValue
import kotlin.random.Random

internal class DebugFlipperFeaturesProvider : DebugDataProvider<Map<String, FlipperValue>> {

    override fun provideData(): Map<String, FlipperValue> {
        return generateTestFeatures()
    }

    private fun generateTestFeatures(): Map<String, FlipperValue> {
        val map = mutableMapOf<String, FlipperValue>()

        map["Show label 1"] = FlipperValue.BooleanValue(false)
        map["Show label 2"] = FlipperValue.BooleanValue(true)
        map["Show label 3"] = FlipperValue.BooleanValue(false)

        val debugRandom = Random(282)
        (4..20).forEach { index ->
            map["Didn't do nothing toggle $index"] =
                FlipperValue.BooleanValue(debugRandom.nextBoolean())
        }

        return map
    }
}
