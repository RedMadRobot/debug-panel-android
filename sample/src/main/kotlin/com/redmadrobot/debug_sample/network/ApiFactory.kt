package com.redmadrobot.debug_sample.network

import com.redmadrobot.debug.plugin.servers.interceptor.DebugServerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
            .addInterceptor(interceptor = DebugServerInterceptor())
            .addInterceptor(
                interceptor = Interceptor { chain ->
                    onCalled.invoke(chain.request().url.toString())
                    chain.proceed(chain.request())
                }
            )
            .build()
    }
}
