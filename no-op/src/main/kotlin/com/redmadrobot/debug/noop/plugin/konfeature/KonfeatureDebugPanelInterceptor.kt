package com.redmadrobot.debug.plugin.konfeature

import android.content.Context
import com.redmadrobot.konfeature.source.FeatureValueSource
import com.redmadrobot.konfeature.source.Interceptor


public class KonfeatureDebugPanelInterceptor(context: Context) : Interceptor {

    override val name: String = "NoopDebugPanelInterceptor"

    override fun intercept(valueSource: FeatureValueSource, key: String, value: Any): Any? = null
}
