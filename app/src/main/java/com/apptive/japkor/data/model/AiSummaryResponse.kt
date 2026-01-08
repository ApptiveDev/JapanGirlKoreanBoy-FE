package com.apptive.japkor.data.model

import com.google.gson.annotations.SerializedName

data class AiSummaryResponse(
    @SerializedName(value = "memberId", alternate = ["member_id"])
    val memberId: Long,
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "aiSummaryKo", alternate = ["ai_summary_ko", "aiSummary", "ai_summary"])
    val aiSummaryKo: String?,
    @SerializedName(value = "aiSummaryJa", alternate = ["ai_summary_ja"])
    val aiSummaryJa: String?
)
