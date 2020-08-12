package com.redmadrobot.servers_plugin.util

import com.redmadrobot.debug_panel_core.extension.getPlugin
import com.redmadrobot.debug_panel_core.internal.DebugPanel
import com.redmadrobot.servers_plugin.plugin.ServersPlugin
import com.redmadrobot.servers_plugin.plugin.ServersPluginContainer
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URI

class DebugServerInterceptor : Interceptor {

    private var requestModifier: ((Request) -> Request?)? = null

    private val panelSettingsRepository by lazy {
        getPlugin<ServersPlugin>()
            .getContainer<ServersPluginContainer>()
            .pluginSettingsRepository
    }

    /**
     * Дополнительная Модификация запроса
     * */
    fun modifyRequest(block: (Request) -> Request): DebugServerInterceptor {
        this.requestModifier = block
        return this
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (DebugPanel.isInitialized()) {
            val debugServer = panelSettingsRepository.getSelectedServerHost()
            if (debugServer != null && debugServer.isNotEmpty()) {
                val newUrl = request.getNewUrl(debugServer)
                request = request.newBuilder()
                    .url(newUrl)
                    .build()
            }
        }
        return chain.proceed(requestModifier?.invoke(request) ?: request)
    }


    private fun Request.getNewUrl(debugServer: String): HttpUrl {
        val serverUri = URI(debugServer)
        return this.url.newBuilder()
            .scheme(serverUri.scheme)
            .host(serverUri.host)
            .build()
    }
}
