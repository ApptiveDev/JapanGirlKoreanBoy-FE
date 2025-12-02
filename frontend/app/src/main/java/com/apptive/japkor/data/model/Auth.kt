package com.apptive.japkor.data.model

// 요청 데이터 모델
data class VerifyEmailCodeRequest(
    val email: String,
    val code: String
)

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)

// 응답 데이터 모델
