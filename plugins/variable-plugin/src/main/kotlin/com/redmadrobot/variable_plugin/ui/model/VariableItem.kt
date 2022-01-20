package com.redmadrobot.variable_plugin.ui.model

import kotlin.reflect.KClass

internal data class VariableItem(
    val name: String,
    val value: String,
    val clazz: KClass<*>,
)
