package com.apptive.japkor.data.api

import com.apptive.japkor.data.model.RequiredInfoDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RequiredInfoApiService {
    @POST("members/me/preferences")
    fun postRequiredInfo(@Body body: RequiredInfoDTO): Call<Void>
}