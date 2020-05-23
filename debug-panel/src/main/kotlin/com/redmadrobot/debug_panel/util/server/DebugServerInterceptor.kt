package com.redmadrobot.debug_panel.util.server

import com.redmadrobot.debug_panel.internal.DebugPanel
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URI


class DebugServerInterceptor : Interceptor {

    private val panelSettingsRepository by lazy {
        DebugPanel.instance?.getContainer()?.panelSettingsRepository
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val debugServer = panelSettingsRepository?.getSelectedServerHost()

        if (debugServer != null && debugServer.isNotEmpty()) {
            val newUrl = request.getNewUrl(debugServer)
            request = request.newBuilder()
                .url(newUrl)
                .build()
        }
        return chain.proceed(request)
    }

    private fun Request.getNewUrl(debugServer: String): HttpUrl {
        val serverUri = URI(debugServer)
        return this.url.newBuilder()
            .scheme(serverUri.scheme)
            .host(serverUri.host)
            .build()
    }
}
