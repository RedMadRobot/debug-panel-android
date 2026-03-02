package com.redmadrobot.debug_sample.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface SampleApi {
    @GET("odos/1")
    suspend fun getTestData(): Response<ResponseBody>
}
