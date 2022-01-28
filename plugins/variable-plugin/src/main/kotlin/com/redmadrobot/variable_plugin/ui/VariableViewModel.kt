package com.redmadrobot.variable_plugin.ui

import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.variable_plugin.data.VariableRepository
import com.redmadrobot.variable_plugin.plugin.VariableItem
import com.redmadrobot.variable_plugin.plugin.VariableSettings
import com.redmadrobot.variable_plugin.plugin.VariableWidget
import com.redmadrobot.variable_plugin.ui.model.VariableEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class VariableViewModel(
    private val repository: VariableRepository,
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

    fun requireVariableSettings(variableName: String): VariableSettings<Any>? {
        return repository.getVariableSettings(variableName)
    }

    fun requireVariableWidget(variableKClassHashCode: Int): VariableWidget<Any> {
        return repository.getVariableWidget(variableKClassHashCode)!!
    }
}
