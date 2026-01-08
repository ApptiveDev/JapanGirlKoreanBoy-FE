package com.apptive.japkor.data.api

import com.apptive.japkor.data.model.AiSummaryResponse
import com.apptive.japkor.data.model.MatchingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.apptive.japkor.data.model.MyStatusResponse

interface MatchingService {
    @GET("members/ai-summary")
    fun getMyAiSummary(): Call<AiSummaryResponse>

    @GET("members/matchings/female")
    fun getFemaleMatchings(): Call<List<MatchingResponse>>

    @POST("members/matchings/{matchingId}/select")
    fun femaleSelectMatching(@Path("matchingId") matchingId: Long): Call<Void>
}
