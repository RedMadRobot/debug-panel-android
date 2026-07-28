package com.redmadrobot.debug.plugin.konfeature

import android.content.Context
import com.redmadrobot.konfeature.Logger
import com.redmadrobot.konfeature.builder.KonfeatureBuilder
import com.redmadrobot.konfeature.ui.KonfeatureDebugInterceptor
import com.redmadrobot.konfeature.ui.KonfeatureDebugStore

/**
 * Everything [KonfeaturePlugin] needs to override feature values, bundled into a single object: the
 * persistent [KonfeatureDebugStore] and the [KonfeatureDebugInterceptor] backed by that same store.
 *
 * Bundling them guarantees the interceptor feeding your Konfeature instance and the panel showing
 * the overrides share one store — the wiring callers previously had to get right by hand.
 *
 * Create it once on startup, attach it to your Konfeature instance with [applyDebugPanelConfig],
 * then hand the same config to [KonfeaturePlugin]:
 *
 * ```kotlin
 * val config = KonfeatureDebugPanelConfig.create(context)
 * val konfeature = konfeature {
 *     register(MyFeatureConfig())
 *     applyDebugPanelConfig(config)
 * }
 * KonfeaturePlugin(konfeature = konfeature, config = config)
 * ```
 *
 * @see applyDebugPanelConfig
 */
public class KonfeatureDebugPanelConfig private constructor(
    internal val store: KonfeatureDebugStore,
    internal val interceptor: KonfeatureDebugInterceptor,
) {
    /**
     * Set to `true` by [applyDebugPanelConfig]. [KonfeaturePlugin] checks it to fail fast when a
     * config was created but never attached to a Konfeature instance — otherwise the panel would
     * display feature values it could never actually override.
     */
    internal var isAttached: Boolean = false

    public companion object {
        /** Default DataStore file name, resolved under `context.filesDir`. */
        public const val DEFAULT_PATH: String = "konfeature_debug.preferences_pb"

        /**
         * Creates the store (completing its initial load of persisted overrides) and the
         * interceptor backed by it.
         *
         * Call once on startup from any suitable coroutine scope and reuse the returned instance
         * for both [applyDebugPanelConfig] and [KonfeaturePlugin].
         *
         * @param context used to resolve [path] under the app's files directory.
         * @param path file name for the DataStore file, relative to `context.filesDir`.
         * @param logger optional logger forwarded to [KonfeatureDebugStore.create].
         */
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
 * Registers the debug panel's interceptor on this builder and marks [config] as attached so
 * [KonfeaturePlugin] can verify the wiring.
 *
 * ```kotlin
 * val konfeature = konfeature {
 *     register(MyFeatureConfig())
 *     applyDebugPanelConfig(config)
 * }
 * ```
 */
public fun KonfeatureBuilder.applyDebugPanelConfig(
    config: KonfeatureDebugPanelConfig,
): KonfeatureBuilder {
    addInterceptor(config.interceptor)
    config.isAttached = true
    return this
}
