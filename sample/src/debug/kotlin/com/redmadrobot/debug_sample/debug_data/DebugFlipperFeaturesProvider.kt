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

        map[object : Feature() {
            override val id: String = "Show label 1"
        }] = FlipperValue.BooleanValue(false)

        map[object : Feature() {
            override val id: String = "Show label 2"
        }] = FlipperValue.BooleanValue(true)

        map[object : Feature() {
            override val id: String = "Show label 3"
        }] = FlipperValue.BooleanValue(false)

        val debugRandom = Random(282)
        (4..20).forEach { index ->
            val feature = object : Feature() {
                override val id: String = "Didn't do nothing toggle $index"
            }

            map[feature] = FlipperValue.BooleanValue(debugRandom.nextBoolean())
        }

        return map
    }
}
