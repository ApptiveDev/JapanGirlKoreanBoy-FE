package com.apptive.japkor.data.api

import com.apptive.japkor.BuildConfig // BuildConfig import 추가
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient{

    val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
}