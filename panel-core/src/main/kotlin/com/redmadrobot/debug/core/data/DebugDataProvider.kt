package com.redmadrobot.debug.core.data

/**
 * Functional interface for lazy data supply to plugins.
 *
 * Allows passing data into a plugin through a provider rather than directly,
 * which is useful when data is produced lazily.
 *
 * Example usage with [com.redmadrobot.debug.plugin.servers.ServersPlugin]:
 * ```
 * val provider = DebugDataProvider<List<DebugServer>> {
 *     listOf(DebugServer("Prod", "https://api.example.com", isDefault = true))
 * }
 * ServersPlugin(preInstalledServers = provider)
 * ```
 *
 * @param T type of provided data
 */
public interface DebugDataProvider<T> {
    /** Returns data of type [T]. */
    public fun provideData(): T
}
