package com.redmadrobot.debug.plugin.servers.interceptor

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

/**
 * OkHttp [Interceptor] that replaces the host and scheme of requests
 * with values of the currently selected server from [ServersPlugin].
 *
 * If the debug panel is not initialized, requests pass through unchanged.
 *
 * Example wiring:
 * ```
 * OkHttpClient.Builder()
 *     .addInterceptor(DebugServerInterceptor())
 *     .build()
 * ```
 *
 * @see ServersPlugin
 * @see DebugServer
 */
public class DebugServerInterceptor : Interceptor {
    private var requestModifier: ((Request, DebugServer) -> Request?)? = null

    private val serverRepository by lazy {
        getPlugin<ServersPlugin>()
            .getContainer<ServersPluginContainer>()
            .serversRepository
    }

    /**
     * Adds an additional request modification after the host substitution.
     *
     * Allows, for example, adding headers specific to a particular server.
     *
     * @param block modification function taking the current request and the selected server
     * @return this interceptor for call chaining (builder pattern)
     */
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
