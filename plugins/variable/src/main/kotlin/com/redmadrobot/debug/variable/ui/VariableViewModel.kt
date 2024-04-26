package com.redmadrobot.debug.variable.ui

import com.redmadrobot.debug.common.base.PluginViewModel
import com.redmadrobot.debug.variable.plugin.VariableItem
import com.redmadrobot.debug.variable.plugin.VariableWidget
import com.redmadrobot.debug.variable.plugin.VariableWidgetSettings
import com.redmadrobot.debug.variable.ui.model.VariableEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class VariableViewModel(
    private val repository: com.redmadrobot.debug.variable.data.VariableRepository,
) : PluginViewModel() {

    val modifiers: Flow<List<VariableItem<Any>>>
        get() = repository.savedVariables.map { it.values.toList() }

    fun onVariableEvent(event: VariableEvent) {
        when (event) {
            is VariableEvent.ValueChanged<*> -> {
                repository.updateVariableValue(
                    variableName = event.variableName,
                    value = event.newValue,
                )
            }

            is VariableEvent.SettingsChanged<*> -> {
                repository.updateVariableSetting(
                    variableName = event.variableName,
                    setting = event.newSettings,
                )
            }

            is VariableEvent.EnabledStatusChanged -> {
                repository.updateEnabledStatus(
                    variableName = event.variableName,
                    enabled = event.enabled
                )
            }
        }
    }

    fun getVariableEnabledStatus(variableName: String): Boolean {
        return repository.getVariableEnabledStatus(variableName)
    }

    fun requireVariableWidgetSettings(variableName: String): VariableWidgetSettings<Any>? {
        return repository.getVariableWidgetSettings(variableName)
    }

    fun requireVariableWidget(variableKClassHashCode: Int): VariableWidget<Any> {
        return repository.getVariableWidget(variableKClassHashCode)!!
    }
}
