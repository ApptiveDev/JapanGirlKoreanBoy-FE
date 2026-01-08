package com.apptive.japkor.data.api

import com.apptive.japkor.data.model.MemberInfoResponse
import retrofit2.Call
import retrofit2.http.GET

interface MemberService {
    @GET("members/info")
    fun getMemberInfo(): Call<MemberInfoResponse>
}
