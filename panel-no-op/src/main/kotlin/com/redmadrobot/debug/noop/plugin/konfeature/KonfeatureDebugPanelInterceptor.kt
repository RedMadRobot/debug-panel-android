package com.redmadrobot.debug.plugin.konfeature

import android.content.Context
import com.redmadrobot.konfeature.source.FeatureValueSource
import com.redmadrobot.konfeature.source.Interceptor

/**
 * No-op implementation of [KonfeatureDebugPanelInterceptor] for release builds.
 *
 * [intercept] always returns `null`, so no flag overrides are applied.
 */
@Suppress("UnusedPrivateProperty")
public class KonfeatureDebugPanelInterceptor(context: Context) : Interceptor {
    override val name: String = "NoopDebugPanelInterceptor"

    override fun intercept(valueSource: FeatureValueSource, key: String, value: Any): Any? = null
}
