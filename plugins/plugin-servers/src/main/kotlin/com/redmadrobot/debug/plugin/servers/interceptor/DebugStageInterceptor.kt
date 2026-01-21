package com.redmadrobot.debug.plugin.servers.interceptor

import com.redmadrobot.debug.core.DebugPanel
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.plugin.servers.ServersPlugin
import com.redmadrobot.debug.plugin.servers.ServersPluginContainer
import com.redmadrobot.debug.plugin.servers.data.model.DebugStage
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URI

public class DebugStageInterceptor(private val hostName: String) : Interceptor {

    private var requestModifier: ((Request, DebugStage) -> Request?)? = null

    private val stageRepository by lazy {
        getPlugin<ServersPlugin>()
            .getContainer<ServersPluginContainer>()
            .stagesRepository
    }

    /**
     * Additional request modification
     * */
    public fun modifyRequest(block: (Request, DebugStage) -> Request): DebugStageInterceptor {
        this.requestModifier = block
        return this
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (!DebugPanel.isInitialized) {
            return chain.proceed(originalRequest)
        }

        val debugStage = stageRepository.getSelectedStage()
        val host = debugStage?.hosts?.get(hostName)
        var currentRequest = originalRequest

        if (host != null) {
            val newUrl = originalRequest.getNewUrl(host)
            if (newUrl != originalRequest.url) {
                currentRequest = originalRequest.newBuilder().url(newUrl).build()
            }
        }

        val modifiedRequest = debugStage?.let { stage ->
            requestModifier?.invoke(currentRequest, stage)
        }

        return chain.proceed(modifiedRequest ?: currentRequest)
    }


    private fun Request.getNewUrl(debugServer: String): HttpUrl {
        val serverUri = URI(debugServer)
        return this.url.newBuilder()
            .scheme(serverUri.scheme)
            .host(serverUri.host)
            .build()
    }
}
