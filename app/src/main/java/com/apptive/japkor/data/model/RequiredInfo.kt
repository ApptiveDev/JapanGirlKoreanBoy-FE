package com.apptive.japkor.data.model

// 전체필수정보 DTO
data class RequiredInfoDTO(
    val name: String,
    val gender: String,
    val height: Int,
    val weight: Int,
    val residenceArea: String,
    val smokingStatus: String,
    val drinkingFrequency: String,
    val religion: String,
    val education: String,
    val asset: String,
    val otherInfo: String,
    val profileImageUrls: List<String>?,
    val thumbnailImageUrl: String?,

    val preferredHeightMin: Int,
    val preferredHeightMax: Int,
    val avoidReligions: List<String>,
    val preferredEducationLevel: String,
    val preferredAppearanceStyle: String,
    val parentAssetRequirement: String,
    val preferredAssetMin: Long,
    val preferredAssetMax: Long,
    val preferredJobs: List<String>,
    val avoidedJobs: List<String>,

    val mbti1: String,
    val mbti2: String,
    val mbti3: String,
    val mbti4: String,

    val priority1: String,
    val priority2: String,
    val priority3: String
)

// 이미지 발급 요청 DTO
data class PresignedUrlRequest(
    val fileName: String,
    val contentType: String
)

// 이미지 업로드 응답 DTO

data class PresignedUrlResponse(
    val fileName: String,
    val presignedUrl: String,
    val contentType: String
)
