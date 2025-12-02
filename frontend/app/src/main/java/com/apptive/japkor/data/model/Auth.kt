package com.apptive.japkor.data.model

// 요청 데이터 모델

data class SendEmailCodeRequest(
    val email: String
)
data class VerifyEmailCodeRequest(
    val email: String,
    val code: String
)

// 회원가입 요청 데이터 모델
data class SignUpDTO(
    val name: String,
    val email: String,
    val password: String
)

