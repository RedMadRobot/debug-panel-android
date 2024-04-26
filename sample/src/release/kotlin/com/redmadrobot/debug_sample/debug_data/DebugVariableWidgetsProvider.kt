package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.core.data.DebugDataProvider
import com.redmadrobot.debug.variable.plugin.VariableWidget

internal class DebugVariableWidgetsProvider : DebugDataProvider<List<VariableWidget<Any>>> {
    override fun provideData(): List<VariableWidget<Any>> {
        return emptyList()
    }
}
