package com.redmadrobot.variable_plugin.plugin

import kotlin.reflect.KClass

public class VariablePlugin(
    private val customWidgets: List<Any> = emptyList(),
)

public abstract class VariableWidget<T : Any>(
    public val kClass: KClass<T>,
)

public fun <T : Any> T.asDebugVariable(
    name: String,
): T = this
