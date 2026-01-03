package com.apptive.japkor.data.api

import com.apptive.japkor.data.model.MatchingResponse
import retrofit2.Call
import retrofit2.http.GET

interface MatchingService {
    @GET("members/matchings/female")
    fun getFemaleMatchings(): Call<List<MatchingResponse>>
}
