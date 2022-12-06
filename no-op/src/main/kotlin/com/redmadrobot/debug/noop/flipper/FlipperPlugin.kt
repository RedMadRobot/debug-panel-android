package com.redmadrobot.debug.flipper.plugin

import com.redmadrobot.flipper.config.FlipperValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

public class FlipperPlugin(
    private val toggles: List<Any> = emptyList(),
) {

    public companion object {
        public fun observeChangedToggles(): Flow<Map<String, FlipperValue>> {
            return emptyFlow()
        }
        public fun addSource(sourceName: String, toggles: Map<String, FlipperValue>) = Unit
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
