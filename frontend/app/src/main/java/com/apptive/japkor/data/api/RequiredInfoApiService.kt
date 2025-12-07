package com.apptive.japkor.data.api

import com.apptive.japkor.data.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RequiredInfoApiService {
    @POST("members/me/preferences")
    fun postRequiredInfo(@Body body: RequiredInfoDTO): Call<Void>

    @POST("image/presigned-url")
    fun getPresignedUrl(@Body body: PresignedUrlRequest): Call<List<PresignedUrlResponse>>
}