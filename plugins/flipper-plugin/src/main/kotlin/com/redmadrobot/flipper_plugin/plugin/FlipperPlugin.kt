package com.redmadrobot.flipper_plugin.plugin

import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.flipper.Feature
import com.redmadrobot.flipper.config.FlipperValue
import com.redmadrobot.flipper_plugin.ui.FlipperFeaturesFragment
import kotlinx.coroutines.flow.Flow
import java.util.*

public class FlipperPlugin(
    private val featureStateMap: Map<Feature, FlipperValue>,
) : Plugin() {

    public companion object {
        private const val NAME = "FLIPPER PLUGIN"

        public fun observeUpdatedToggle(): Flow<Pair<Feature, FlipperValue>> {
            return FlipperPluginTogglesStateDispatcher.observeUpdatedToggle()
        }

        public fun observeMultipleTogglesChanged(): Flow<Map<Feature, FlipperValue>> {
            return FlipperPluginTogglesStateDispatcher.observeMultipleTogglesChanged()
        }
    }

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return FlipperPluginContainer(
            context = commonContainer.context,
            defaultFeaturesMap = Collections.unmodifiableMap(HashMap(featureStateMap)),
        )
    }

    override fun getFragment(): Fragment {
        return FlipperFeaturesFragment()
    }
}
