package com.apptive.japkor.data.api

import com.apptive.japkor.data.model.AiSummaryResponse
import com.apptive.japkor.data.model.UserInfoResponse
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("members/ai-summary")
    fun getMyAiSummary(): Call<AiSummaryResponse>

    @GET("user/info")
    fun getUserInfo(): Call<UserInfoResponse>
}
