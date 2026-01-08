package com.apptive.japkor.data.api

import com.apptive.japkor.data.model.MalePendingMatchingResponse
import com.apptive.japkor.data.model.MatchingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MatchingService {

    // 여자 전용
    @GET("members/matchings/female")
    fun getFemaleMatchings(): Call<List<MatchingResponse>>

    @POST("members/matchings/{matchingId}/select")
    fun femaleSelectMatching(@Path("matchingId") matchingId: Long): Call<Void>

    // 남자 전용
    @GET("members/matchings/male/pendingMatching")
    fun getMalePendingMatchings(): Call<List<MalePendingMatchingResponse>>

    @POST("members/matchings/{matchingId}/accept")
    fun maleAcceptMatching(@Path("matchingId") matchingId: Long): Call<Void>

    @POST("members/matchings/{matchingId}/reject")
    fun maleRejectMatching(@Path("matchingId") matchingId: Long): Call<Void>
}
