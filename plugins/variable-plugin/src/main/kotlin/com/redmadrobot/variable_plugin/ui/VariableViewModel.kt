package com.redmadrobot.variable_plugin.ui

import com.redmadrobot.debug_panel_common.base.PluginViewModel
import com.redmadrobot.variable_plugin.data.VariableRepository
import com.redmadrobot.variable_plugin.ui.model.VariableEvent
import com.redmadrobot.variable_plugin.ui.model.VariableItem
import com.redmadrobot.variable_plugin.ui.model.VariableSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class VariableViewModel(
    private val repository: VariableRepository,
) : PluginViewModel() {

    val modifiers: Flow<List<VariableItem>>
        get() = repository.modifiers.map { it.values.toList() }

    fun onVariableEvent(event: VariableEvent) {
        when (event) {
            is VariableEvent.ValueChange -> {
                repository.updateVariableValue(
                    variableName = event.variableName,
                    value = event.newValue,
                )
            }

            is VariableEvent.SettingChanged -> {
                repository.updateVariableSetting(
                    variableName = event.variableName,
                    setting = event.setting,
                )
            }
        }
    }

    fun getVariableSettings(variableName: String): VariableSettings {
        return repository.getVariableSettings(variableName)
    }
}
