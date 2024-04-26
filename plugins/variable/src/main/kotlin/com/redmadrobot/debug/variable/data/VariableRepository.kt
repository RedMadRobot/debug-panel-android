package com.redmadrobot.debug.variable.data

import androidx.collection.ArrayMap
import com.redmadrobot.debug.variable.plugin.VariableItem
import com.redmadrobot.debug.variable.plugin.VariableWidget
import com.redmadrobot.debug.variable.plugin.VariableWidgetSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.reflect.KClass

internal class VariableRepository {

    private val supportedClasses = ArrayMap<Int, VariableWidget<Any>>()
    private val supportedClassesSettings = ArrayMap<Int, VariableWidgetSettings<Any>>()

    private val _savedVariables = MutableStateFlow<MutableMap<String, VariableItem<Any>>>(
        mutableMapOf()
    )
    val savedVariables: StateFlow<Map<String, VariableItem<Any>>> = _savedVariables.asStateFlow()

    private val VariableWidgetSettings: MutableMap<String, VariableWidgetSettings<Any>> = mutableMapOf()
    private val variableEnabledStatus: MutableMap<String, Boolean> = mutableMapOf()

    fun <T> updateVariableValue(
        variableName: String,
        value: T,
    ) {
        _savedVariables.value[variableName]
            ?.copy(value = value as Any)
            ?.let { updatedValue ->
                val updatedMap = _savedVariables.value.also { variablesMap ->
                    variablesMap[variableName] = updatedValue
                }
                _savedVariables.tryEmit(updatedMap)
            }
    }

    fun <T : Any> updateVariableSetting(
        variableName: String,
        setting: VariableWidgetSettings<T>,
    ) {
        require(variableName in VariableWidgetSettings.keys)

        VariableWidgetSettings[variableName] = setting as VariableWidgetSettings<Any>
    }

    fun getVariableWidgetSettings(variableName: String): VariableWidgetSettings<Any>? {
        return VariableWidgetSettings[variableName]
    }

    fun updateEnabledStatus(
        variableName: String,
        enabled: Boolean,
    ) {
        variableEnabledStatus[variableName] = enabled
    }

    fun getVariableEnabledStatus(variableName: String): Boolean {
        return variableEnabledStatus[variableName] ?: true
    }

    fun getVariableWidget(variableKClassHashCode: Int): VariableWidget<Any>? {
        return supportedClasses[variableKClassHashCode]
    }

    fun <T : Any> addWidget(widget: VariableWidget<T>) {
        supportedClasses[widget.kClass.hashCode()] = widget as VariableWidget<Any>
    }

    fun <T : Any> getDebugVariableValue(
        name: String,
        defaultValue: T,
        variableClass: KClass<T>,
    ): T {
        val variableClassHashCode = variableClass.hashCode()

        require(variableClass.isInstance(defaultValue))
        require(variableClassHashCode in supportedClasses.keys)

        val variables = _savedVariables.value
        if (variables[name]?.kClass != variableClass) {
            createNewVariable(name, defaultValue, variableClass)
        }

        if (!variableEnabledStatus[name]!!) return defaultValue

        val variable = variables[name]!!
        val updatedVariable = VariableWidgetSettings[name]?.apply(variable) ?: variable
        if (updatedVariable != variable) {
            updateVariableValue(name, updatedVariable.value)
        }

        return updatedVariable.value as T
    }

    private fun <T : Any> createNewVariable(
        name: String,
        defaultValue: T,
        variableClass: KClass<T>,
    ) {
        val variables = _savedVariables.value
        variables[name] = VariableItem(
            name = name,
            value = defaultValue,
            kClass = variableClass as KClass<Any>,
        )

        getVariableWidget(variableClass.hashCode())!!
            .getSupportedSettings()
            ?.let { supportedSettings ->
                VariableWidgetSettings[name] = supportedSettings
            }

        variableEnabledStatus[name] = true

        _savedVariables.tryEmit(variables)
    }
}
