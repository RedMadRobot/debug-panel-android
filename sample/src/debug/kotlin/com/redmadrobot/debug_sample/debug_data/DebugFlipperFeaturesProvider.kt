package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import kotlin.random.Random

internal class DebugFlipperFeaturesProvider : DebugDataProvider<Map<Feature, FlipperValue>> {

    override fun provideData(): Map<Feature, FlipperValue> {
        return generateTestFeatures()
    }

    private fun generateTestFeatures(): Map<Feature, FlipperValue> {
        val map = mutableMapOf<Feature, FlipperValue>()

        (1..20).forEach { index ->
            val feature = object : Feature() {
                override val id: String = "Feature toggle $index"
            }

            map[feature] = FlipperValue.BooleanValue(Random.nextBoolean())
        }

        return map
    }
}
