package com.apptive.japkor.data.api

object ServiceFactory {
    val authApiService: AuthApiService by lazy {
        ApiClient.retrofit.create(AuthApiService::class.java)
    }

    val requiredInfoApiService: RequiredInfoApiService by lazy {
        ApiClient.retrofit.create(RequiredInfoApiService::class.java)
    }
}