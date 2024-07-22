package com.redmadrobot.debug.plugin.variable

import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import com.redmadrobot.debug.core.internal.CommonContainer
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.plugin.Plugin
import com.redmadrobot.debug.core.internal.PluginDependencyContainer
import com.redmadrobot.debug.plugin.variable.databinding.FragmentContainerVariableBinding
import com.redmadrobot.debug.plugin.variable.ui.VariableFragment
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

    @Deprecated(
        "You should't use fragments for you plugins. Please use Jetpack Compose",
        replaceWith = ReplaceWith("content()", "com.redmadrobot.debug.core.plugin.Plugin")
    )
    override fun getFragment(): Fragment {
        return VariableFragment()
    }

    @Composable
    override fun content() {
        AndroidViewBinding(
            FragmentContainerVariableBinding::inflate,
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}

public fun <T : Any> T.asDebugVariable(
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
    ): VariableWidgetSettingsViewHolder<T, VariableWidgetSettings<T>>? = null

    public open fun getSupportedSettings(): VariableWidgetSettings<T>? = null
}

/**
 * This class provides a way to make your widget customisable on the fly
 * Ex. autoincrement integer, random day of year in date, etc.
 */
public abstract class VariableWidgetSettings<T : Any> {

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

public abstract class VariableWidgetSettingsViewHolder<T : Any, TSettings : VariableWidgetSettings<T>>(
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
