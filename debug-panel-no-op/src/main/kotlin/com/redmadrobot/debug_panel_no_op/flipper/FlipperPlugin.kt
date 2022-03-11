package com.redmadrobot.flipper_plugin.plugin

import com.redmadrobot.flipper.config.FlipperValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public class FlipperPlugin(
    private val toggles: List<PluginToggle> = emptyList(),
) {

    public companion object {
        public fun observeChangedToggles(): Flow<Map<String, FlipperValue>> {
            return emptyFlow()
        }
    }

    @Deprecated(message = "Use primary constructor")
    public constructor(
        featureStateMap: Map<String, FlipperValue>,
    ) : this(emptyList())
}

public data class PluginToggle(
    val id: String,
    val group: String,
    val value: FlipperValue,
    val description: String,
)
