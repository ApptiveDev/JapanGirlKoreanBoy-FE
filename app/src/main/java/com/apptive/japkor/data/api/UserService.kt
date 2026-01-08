package com.apptive.japkor.data.api

import com.apptive.japkor.data.model.MemberInfoResponse
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("members/info")
    fun getUserInfo(): Call<MemberInfoResponse>
}
