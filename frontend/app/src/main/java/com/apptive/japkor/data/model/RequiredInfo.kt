package com.apptive.japkor.data.model

data class RequiredInfoDTO(
    val gender: String?,
    val height: Int?,
    val weight: Int?,
    val residenceArea: String?,
    val smokingStatus: String?,
    val drinkingFrequency: String?,
    val religion: String?,
    val education: String?,
    val asset: String?,
    val otherInfo: String?,
    val profileImageUrls: List<String>?,
    val thumbnailImageUrl: String?,

    val preferredHeightMin: Int?,
    val preferredHeightMax: Int?,
    val avoidReligions: List<String>?,
    val preferredEducationLevel: String?,
    val preferredAppearanceStyle: String?,
    val parentAssetRequirement: String?,
    val preferredAssetMin: Long?,
    val preferredAssetMax: Long?,
    val preferredJobs: List<String>?,
    val avoidedJobs: List<String>?,

    val mbti1: String?,
    val mbti2: String?,
    val mbti3: String?,
    val mbti4: String?,

    val priority1: String?,
    val priority2: String?,
    val priority3: String?
)
