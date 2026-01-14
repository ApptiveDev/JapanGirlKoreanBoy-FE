package com.apptive.japkor.data.model

import com.google.gson.annotations.SerializedName

data class MatchingResponse(
    val matchingId: Long,
    val maleMemberId: Long,
    val maleName: String,
    val maleEmail: String,
    val height: Int?,
    val weight: Int?,
    val residenceArea: String?,
    val thumbnailImageUrl: String?,
    val matchingOrder: Int,
    val status: String
)

data class MalePendingMatchingResponse(
    val matchingId: Long,
    val femaleMemberId: Long,
    val femaleName: String,
    val femaleEmail: String,
    val height: Int?,
    val weight: Int?,
    val residenceArea: String?,
    val aiSummary: String?,
    val status: String,
    val createdAt: String,
    val thumbnailImageUrl: String?,
    @SerializedName(value = "profileImageUrls", alternate = ["profileImageUrl"])
    val profileImageUrls: List<String>?
)

data class HomeMatching(
    val matchingId: Long,
    val memberId: Long,
    val name: String,
    val email: String,
    val thumbnailImageUrl: String?,
    val profileImageUrl: String?,
    val height: Int?,
    val weight: Int?,
    val residenceArea: String?,
    val matchingOrder: Int?,
    val status: String
)
