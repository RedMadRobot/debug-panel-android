package com.redmadrobot.variable_plugin.data

import com.redmadrobot.variable_plugin.ui.model.VariableItem
import com.redmadrobot.variable_plugin.ui.model.VariableSetting
import com.redmadrobot.variable_plugin.ui.model.VariableSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.reflect.KClass

internal class VariableRepository {

    private val _modifiers: MutableStateFlow<MutableMap<String, VariableItem>> = MutableStateFlow(
        mutableMapOf()
    )
    val modifiers = _modifiers.asStateFlow()

    private val settings: MutableMap<String, VariableSettings> = mutableMapOf()

    fun updateVariableValue(
        variableName: String,
        value: String,
    ) {
        _modifiers.value[variableName]?.let { updatedVariable ->
            _modifiers.value[updatedVariable.name] = updatedVariable.copy(value = value)
        }
    }

    fun updateVariableSetting(
        variableName: String,
        setting: VariableSetting,
    ) {
        val isEnabled = (setting as? VariableSetting.Enabled)?.isEnabled
        val autoincrement = setting as? VariableSetting.Autoincrement

        settings[variableName]?.let { variableSettings ->
            settings[variableName] = variableSettings.copy(
                isEnabled = isEnabled ?: variableSettings.isEnabled,
                autoincrement = autoincrement ?: variableSettings.autoincrement,
            )
        }
    }

    fun getVariableSettings(variableName: String): VariableSettings {
        return settings[variableName] ?: VariableSettings.DEFAULT
    }

    fun <T> getDebugVariableValue(
        name: String,
        defaultValue: T,
        variableClass: KClass<*>,
    ): T {
        require(variableClass.isInstance(defaultValue))

        if (variableClass == Boolean::class && name in settings.keys) {
            return settings[name]?.isEnabled as? T ?: defaultValue
        }

        val variableSettings = settings[name] ?: VariableSettings.DEFAULT

        if (!variableSettings.isEnabled) return defaultValue

        val savedValue = _modifiers.value.getOrPut(name) {
            settings[name] = if (variableClass == Boolean::class) {
                VariableSettings.DEFAULT.copy(isEnabled = defaultValue as Boolean)
            } else {
                VariableSettings.DEFAULT
            }

            VariableItem(name, defaultValue.toString(), variableClass.simpleName!!)
        }.value

        val incrementStep = if (variableSettings.autoincrement.isEnabled) {
            variableSettings.autoincrement.step
        } else {
            .0
        }

        val updatedSavedValue = when (variableClass) {
            Int::class -> savedValue.toIntOrNull()?.plus(incrementStep)?.toInt()
            Long::class -> savedValue.toLongOrNull()?.plus(incrementStep)?.toLong()
            Short::class -> savedValue.toShortOrNull()?.plus(incrementStep)?.toInt()?.toShort()
            Float::class -> savedValue.toFloatOrNull()?.plus(incrementStep)?.toFloat()
            Double::class -> savedValue.toDoubleOrNull()?.plus(incrementStep)
            Boolean::class -> settings[name]?.isEnabled ?: this
            else -> savedValue
        } as T

        _modifiers.value[name] = VariableItem(
            name = name,
            value = updatedSavedValue.toString(),
            className = variableClass.simpleName!!,
        )

        _modifiers.tryEmit(_modifiers.value)

        return updatedSavedValue ?: defaultValue
    }
}
