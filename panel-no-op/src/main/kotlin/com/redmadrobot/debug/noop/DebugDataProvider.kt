package com.redmadrobot.debug.core.data

/**
 * No-op declaration of [DebugDataProvider] for release builds.
 */
public interface DebugDataProvider<T> {
    public fun provideData(): T
}
