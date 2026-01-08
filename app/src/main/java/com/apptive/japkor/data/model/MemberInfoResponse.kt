package com.apptive.japkor.data.model

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
