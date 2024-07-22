package com.redmadrobot.debug.plugin.variable

import kotlin.reflect.KClass

public class VariablePlugin(
    private val customWidgets: List<Any> = emptyList(),
)

public abstract class VariableWidget<T : Any>(
    public val kClass: KClass<T>
)

public fun <T : Any> T.asDebugVariable(name: String): T = this
