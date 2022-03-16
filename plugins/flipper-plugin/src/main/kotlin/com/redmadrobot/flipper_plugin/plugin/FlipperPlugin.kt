package com.redmadrobot.flipper_plugin.plugin

import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.ui.FlipperFeaturesFragment
import kotlinx.coroutines.flow.Flow
import java.util.*

public class FlipperPlugin(
    private val toggles: List<PluginToggle>,
) : Plugin() {

    public companion object {
        private const val NAME = "FLIPPER PLUGIN"

        private val featureTogglesRepository by lazy(LazyThreadSafetyMode.NONE) {
            getPlugin<FlipperPlugin>()
                .getContainer<FlipperPluginContainer>()
                .featureTogglesRepository
        }

        public fun observeChangedToggles(): Flow<Map<String, FlipperValue>> {
            return featureTogglesRepository.observeChangedToggles()
        }

        public fun addSource(sourceName: String, toggles: Map<String, FlipperValue>) {
            featureTogglesRepository.addSource(sourceName, toggles)
        }
    }

    @Deprecated(message = "Use primary constructor")
    public constructor(
        featureStateMap: Map<String, FlipperValue>,
    ) : this(featureStateMap.map { (id, value) ->
        PluginToggle(
            id = id,
            group = "",
            value = value,
            description = id,
        )
    })

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return FlipperPluginContainer(
            context = commonContainer.context,
            defaultToggles = Collections.unmodifiableList(ArrayList(toggles)),
        )
    }

    override fun getFragment(): Fragment {
        return FlipperFeaturesFragment()
    }
}

public data class PluginToggle(
    val id: String,
    val group: String,
    val value: FlipperValue,
    val description: String,
    val editable: Boolean = true,
)
