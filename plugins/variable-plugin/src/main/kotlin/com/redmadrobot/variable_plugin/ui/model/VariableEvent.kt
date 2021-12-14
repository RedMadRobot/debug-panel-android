package com.redmadrobot.variable_plugin.ui.model

internal sealed interface VariableEvent {

    data class ValueChange(
        val variableName: String,
        val newValue: String,
    ) : VariableEvent

    data class SettingChanged(
        val variableName: String,
        val setting: VariableSetting,
    ) : VariableEvent
}
