package com.redmadrobot.debug_panel_core.data

interface DebugDataProvider<T> {
    fun provideData(): T
}

