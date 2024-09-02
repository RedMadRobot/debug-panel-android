package com.redmadrobot.debug.core.data

interface DebugDataProvider<T> {
    fun provideData(): T
}

