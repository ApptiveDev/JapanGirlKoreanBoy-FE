package com.apptive.japkor.data.api

import com.apptive.japkor.data.local.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenProvider.getToken()
        val newRequest = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()
        return chain.proceed(newRequest)
    }
}
