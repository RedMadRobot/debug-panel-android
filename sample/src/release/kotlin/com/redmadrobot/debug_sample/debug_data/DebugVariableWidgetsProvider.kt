package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug_panel_core.data.DebugDataProvider
import com.redmadrobot.variable_plugin.plugin.VariableWidget

internal class DebugVariableWidgetsProvider : DebugDataProvider<List<VariableWidget<Any>>> {
    override fun provideData(): List<VariableWidget<Any>> {
        return emptyList()
    }
}
