package com.redmadrobot.variable_plugin.ui.model

internal data class VariableSettings(
    val isEnabled: Boolean,
    val autoincrement: VariableSetting.Autoincrement,
) {

    companion object {

        val DEFAULT
            get() = VariableSettings(
                isEnabled = true,
                autoincrement = VariableSetting.Autoincrement(
                    isEnabled = false,
                    step = 1.0,
                )
            )
    }
}
