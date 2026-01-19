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
        var request = chain.request()
        return if (DebugPanel.isInitialized) {
            val debugStage = stageRepository.getSelectedStage()
            val modifiedRequest = debugStage?.let { stage ->
                val host = stage.hosts[hostName]
                if (host != null) {
                    val newUrl = request.getNewUrl(host)
                    request = request.newBuilder()
                        .url(newUrl)
                        .build()
                }

                requestModifier?.invoke(request, stage)
            } ?: request

            chain.proceed(modifiedRequest)
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
