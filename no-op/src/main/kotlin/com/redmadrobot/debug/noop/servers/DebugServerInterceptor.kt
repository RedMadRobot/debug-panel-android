package com.redmadrobot.servers_plugin.util

import com.redmadrobot.servers_plugin.data.model.DebugServer
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class DebugServerInterceptor : Interceptor {

    fun modifyRequest(block: (Request, DebugServer) -> Request): DebugServerInterceptor {
        return this
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}
