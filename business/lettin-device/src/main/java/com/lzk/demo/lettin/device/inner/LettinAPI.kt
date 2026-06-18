package com.lzk.demo.lettin.device.inner

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LettinAPI {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("api")
    suspend fun request(
        @Body body: RequestBody,
    ): Any
}
