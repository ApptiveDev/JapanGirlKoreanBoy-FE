package com.apptive.japkor.data.model

import com.google.gson.annotations.SerializedName

data class AiSummaryResponse(
    @SerializedName(value = "memberId", alternate = ["member_id"])
    val memberId: Long,
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "aiSummary", alternate = ["ai_summary"])
    val aiSummary: String
)
