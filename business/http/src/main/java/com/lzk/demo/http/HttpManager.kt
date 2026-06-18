package com.lzk.demo.http

import com.lzk.core.log.logD
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HttpManager {
    private const val TAG = "HttpManager"
    private const val CONNECT_TIME_OUT = 10L
    private const val READ_TIME_OUT = 10L
    private const val WRITE_TIME_OUT = 10L

    private var loggingInterceptor =
        HttpLoggingInterceptor { message ->
            logD(TAG, "MyHttpLogger--$message")
        }

    private val httpClient: OkHttpClient by lazy {
        val (sslFactory, trustManager) = SSLHelper.getSSLFactoryAndTrustManager()
        OkHttpClient
            .Builder()
            .sslSocketFactory(sslFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    fun createHttpService(baseUrl: String): Retrofit =
        Retrofit
            .Builder()
            .apply {
                logD(TAG, "createHttpService:$baseUrl")
                baseUrl(formatUrl(baseUrl))
                addConverterFactory(GsonConverterFactory.create())
                client(httpClient)
            }.build()

    private fun formatUrl(url: String): String =
        if (!url.contains("https://")) {
            "https://$url/"
        } else {
            url
        }
}
