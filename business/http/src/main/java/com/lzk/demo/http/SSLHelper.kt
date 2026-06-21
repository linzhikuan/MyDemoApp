package com.lzk.demo.http

import android.annotation.SuppressLint
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object SSLHelper {
    fun getSSLFactoryAndTrustManager(): Pair<SSLSocketFactory, X509TrustManager> {
        val trustManager =
            @SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String,
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String,
                ) {
                }
            }
        val trustAllCerts = arrayOf<TrustManager>(trustManager)
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        return Pair(sslContext.socketFactory, trustManager)
    }
}
