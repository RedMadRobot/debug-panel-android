package com.redmadrobot.variable_plugin.ui.model

import com.redmadrobot.variable_plugin.plugin.VariableWidgetSettings

internal sealed interface VariableEvent {

    data class ValueChanged<T>(
        val variableName: String,
        val newValue: T,
    ) : VariableEvent

    data class SettingsChanged<T : Any>(
        val variableName: String,
        val newSettings: VariableWidgetSettings<T>,
    ) : VariableEvent

    data class EnabledStatusChanged(
        val variableName: String,
        val enabled: Boolean,
    ) : VariableEvent
}
