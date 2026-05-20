package com.redmadrobot.debug.core

/**
 * Marker interface for events emitted by plugins.
 *
 * Plugins send events via [com.redmadrobot.debug.core.plugin.Plugin.pushEvent],
 * consumers subscribe via [DebugPanel.subscribeToEvents] or [DebugPanel.observeEvents].
 *
 * Example:
 * ```
 * data class ServerSelectedEvent(val server: DebugServer) : DebugEvent
 * ```
 *
 * @see com.redmadrobot.debug.core.plugin.Plugin.pushEvent
 * @see DebugPanel.observeEvents
 */
public interface DebugEvent
