package com.redmadrobot.variable_plugin.plugin

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.redmadrobot.debug_panel_core.CommonContainer
import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.debug_panel_core.plugin.Plugin
import com.redmadrobot.debug_panel_core.plugin.PluginDependencyContainer
import com.redmadrobot.variable_plugin.ui.VariableFragment
import kotlin.reflect.KClass

public class VariablePlugin(
    private val customWidgets: List<VariableWidget<Any>> = emptyList(),
) : Plugin() {

    public companion object {
        internal const val NAME = "VARIABLE"
    }

    override fun getName(): String = NAME

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return VariablePluginContainer(
            container = commonContainer,
            customWidgets = customWidgets,
        )
    }

    override fun getFragment(): Fragment {
        return VariableFragment()
    }
}

public fun <T : Any> T.toDebugVariable(
    name: String,
): T {
    return variableRepository.getDebugVariableValue(
        name = name,
        defaultValue = this,
        variableClass = this::class as KClass<T>,
    )
}

private val variableRepository by lazy(LazyThreadSafetyMode.NONE) {
    getPlugin<VariablePlugin>()
        .getContainer<VariablePluginContainer>()
        .variableRepository
}

public abstract class VariableWidget<T : Any>(
    public val kClass: KClass<T>,
) {
    public abstract fun createViewHolder(parent: ViewGroup): VariableWidgetViewHolder<T>

    public open fun createSettingsViewHolder(
        parent: ViewGroup
    ): VariableWidgetSettingsViewHolder<T, VariableSettings<T>>? = null

    public open fun getSupportedSettings(): VariableSettings<T>? = null
}

public abstract class VariableSettings<T : Any> {

    public abstract fun apply(item: VariableItem<T>): VariableItem<T>?
}

public abstract class VariableWidgetViewHolder<T : Any>(
    public val itemView: View,
) {
    public abstract fun bind(
        item: VariableItem<T>,
        onValueChanged: (T) -> Unit,
    )
}

public abstract class VariableWidgetSettingsViewHolder<T : Any, TSettings : VariableSettings<T>>(
    public val itemView: View,
) {
    public abstract fun bind(
        settings: TSettings,
        onSettingsChanged: (TSettings) -> Unit,
    )
}

public data class VariableItem<T : Any>(
    val name: String,
    val value: T,
    val kClass: KClass<T>,
)
