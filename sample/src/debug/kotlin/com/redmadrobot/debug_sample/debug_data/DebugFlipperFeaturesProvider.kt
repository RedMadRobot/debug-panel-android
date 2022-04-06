package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.plugin.PluginToggle
import kotlin.random.Random

internal class DebugFlipperFeaturesProvider : DebugDataProvider<List<PluginToggle>> {

    override fun provideData(): List<PluginToggle> {
        return generateTestFeatures()
    }

    private fun generateTestFeatures(): List<PluginToggle> {
        val toggles = mutableListOf<PluginToggle>()

        toggles.add(
            PluginToggle(
                id = "id1",
                group = "True keen",
                value = FlipperValue.BooleanValue(false),
                description = "Show label 1",
            )
        )
        toggles.add(
            PluginToggle(
                id = "id2",
                group = "True keen",
                value = FlipperValue.BooleanValue(true),
                description = "Show label 2",
            )
        )
        toggles.add(
            PluginToggle(
                id = "id3",
                group = "True keen",
                value = FlipperValue.BooleanValue(false),
                description = "Show label 3",
            )
        )

        val debugRandom = Random(282)
        (4..20).forEach { index ->
            toggles.add(
                PluginToggle(
                    id = "id$index",
                    group = "Didn't do nothing",
                    value = FlipperValue.BooleanValue(debugRandom.nextBoolean()),
                    description = "Didn't do nothing toggle $index",
                )
            )
        }

        return toggles
    }
}
