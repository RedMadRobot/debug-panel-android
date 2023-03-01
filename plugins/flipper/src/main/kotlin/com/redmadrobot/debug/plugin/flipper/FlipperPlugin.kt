package com.redmadrobot.debug.plugin.flipper

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.flipper.ui.FlipperFeatureScreen
import com.redmadrobot.flipper.config.FlipperValue
import kotlinx.coroutines.flow.Flow
import java.util.Collections

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

    public constructor(
        featureStateMap: Map<String, FlipperValue>,
        groupResolver: ((featureId: String) -> String)? = null,
    ) : this(featureStateMap.map { (id, value) ->
        PluginToggle(
            id = id,
            group = groupResolver?.invoke(id).orEmpty(),
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

    @Composable
    override fun content() {
        FlipperFeatureScreen()
    }
}

public data class PluginToggle(
    val id: String,
    val group: String,
    val value: FlipperValue,
    val description: String,
    val editable: Boolean = true,
)
