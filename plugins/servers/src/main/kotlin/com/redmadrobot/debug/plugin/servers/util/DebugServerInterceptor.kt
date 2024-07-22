package com.redmadrobot.debug.plugin.servers.util

import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug.plugin.servers.ServersPluginContainer
import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URI

public class DebugServerInterceptor : Interceptor {

    private var requestModifier: ((Request, DebugServer) -> Request?)? = null

    private val serverRepository by lazy {
        getPlugin<ServersPlugin>()
            .getContainer<ServersPluginContainer>()
            .serversRepository
    }

    /**
     * Дополнительная Модификация запроса
     * */
    public fun modifyRequest(block: (Request, DebugServer) -> Request): DebugServerInterceptor {
        this.requestModifier = block
        return this
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        return if (DebugPanel.isInitialized) {
            val debugServer = runBlocking { serverRepository.getSelectedServer() }
            if (debugServer.url.isNotEmpty()) {
                val newUrl = request.getNewUrl(debugServer.url)
                request = request.newBuilder()
                    .url(newUrl)
                    .build()
            }
            chain.proceed(requestModifier?.invoke(request, debugServer) ?: request)
        } else {
            chain.proceed(request)
        }
    }


    private fun Request.getNewUrl(debugServer: String): HttpUrl {
        val serverUri = URI(debugServer)
        return this.url.newBuilder()
            .scheme(serverUri.scheme)
            .host(serverUri.host)
            .build()
    }
}
