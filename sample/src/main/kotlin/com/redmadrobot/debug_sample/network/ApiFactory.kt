package com.redmadrobot.debug_sample.network

import com.redmadrobot.servers_plugin.util.DebugServerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit

object ApiFactory {

    fun getSampleApi(onCalled: (String) -> Unit): SampleApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(getSampleClient(onCalled))
            .build()

        return retrofit.create(SampleApi::class.java)
    }

    private fun getSampleClient(onCalled: (String) -> Unit): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                DebugServerInterceptor().modifyRequest { request, server ->
                    if (server.name == "Test") {
                        request.newBuilder()
                            .addHeader("Authorization", "testToken")
                            .build()
                    } else {
                        request
                    }
                }
            )
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    onCalled.invoke(chain.request().url.toString())
                    return chain.proceed(chain.request())
                }
            })
            .build()
    }
}
