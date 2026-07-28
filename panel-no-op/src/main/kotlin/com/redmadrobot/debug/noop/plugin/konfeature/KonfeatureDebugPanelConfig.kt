package com.redmadrobot.debug.plugin.konfeature

import android.content.Context
import com.redmadrobot.konfeature.Logger
import com.redmadrobot.konfeature.builder.KonfeatureBuilder
import com.redmadrobot.konfeature.ui.KonfeatureDebugInterceptor
import com.redmadrobot.konfeature.ui.KonfeatureDebugStore

/**
 * No-op counterpart of the `plugin-konfeature` `KonfeatureDebugPanelConfig`, for release builds.
 *
 * Mirrors the real public API so call sites compile unchanged, but the bundled store and
 * interceptor come from `konfeature-ui-noop`: the store holds no overrides and the interceptor
 * never overrides a value.
 */
@Suppress("UnusedPrivateProperty")
public class KonfeatureDebugPanelConfig private constructor(
    internal val store: KonfeatureDebugStore,
    internal val interceptor: KonfeatureDebugInterceptor,
) {
    public companion object {
        /** Default DataStore file name; accepted only to match the real API. */
        public const val DEFAULT_PATH: String = "konfeature_debug.preferences_pb"

        /** Returns a no-op config. [context], [path] and [logger] are accepted only for API parity. */
        public suspend fun create(
            context: Context,
            path: String = DEFAULT_PATH,
            logger: Logger? = null,
        ): KonfeatureDebugPanelConfig {
            val store = KonfeatureDebugStore.create(
                path = context.filesDir.resolve(path).absolutePath,
                logger = logger,
            )
            return KonfeatureDebugPanelConfig(
                store = store,
                interceptor = KonfeatureDebugInterceptor(store),
            )
        }
    }
}

/**
 * No-op counterpart of the `plugin-konfeature` `applyDebugPanelConfig`. Registers the no-op
 * interceptor (which never overrides a value) so the builder stays valid; changes nothing else.
 */
public fun KonfeatureBuilder.applyDebugPanelConfig(
    config: KonfeatureDebugPanelConfig,
): KonfeatureBuilder {
    addInterceptor(config.interceptor)
    return this
}
