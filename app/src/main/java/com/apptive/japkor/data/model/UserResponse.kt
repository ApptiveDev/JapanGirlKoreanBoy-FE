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

data class MemberInfoResponse(
    val memberId: Long,
    val provider: String,
    val providerId: String?,
    val status: UserStatus,
    val name: String?,
    val email: String,
    val gender: String,
    val height: Int?,
    val weight: Int?,
    val residenceArea: String?,
    val smokingStatus: String?,
    val drinkingFrequency: String?,
    val religion: String?,
    val education: String?,
    val asset: String?,
    val otherInfo: String?,
    val thumbnailImageUrl: String?,
    val profileImageUrls: List<String>?,
    val aiSummary: String?,
    val aiSummaryJp: String?,
    val createdAt: String,
    val updatedAt: String
)
