package com.redmadrobot.debug.servers.interceptor

import com.redmadrobot.debug.servers.data.model.DebugStage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


public class DebugStageInterceptor(private val tag: String) : Interceptor {

    public fun modifyRequest(block: (Request, DebugStage) -> Request): DebugStageInterceptor {
        return this
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}
