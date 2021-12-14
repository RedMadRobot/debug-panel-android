package com.redmadrobot.variable_plugin.ui.model

internal sealed interface VariableSetting {

    data class Enabled(val isEnabled: Boolean) : VariableSetting

    data class Autoincrement(val isEnabled: Boolean, val step: Double) : VariableSetting
}
