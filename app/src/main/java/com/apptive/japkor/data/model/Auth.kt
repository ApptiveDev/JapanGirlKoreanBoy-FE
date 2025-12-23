package com.apptive.japkor.data.model

import com.apptive.japkor.utils.constants.UserStatus

// 요청 데이터 모델

data class SendEmailCodeRequest(
    val email: String
)
data class VerifyEmailCodeRequest(
    val email: String,
    val code: String
)

// 응답 데이터 모델

data class SignInResponse(
   val memberId: Int,
   val name: String,
   val token: String,
   val status: UserStatus
)

// 회원가입 요청 데이터 모델
data class SignUpDTO(
    val name: String,
    val email: String,
    val password: String
)

data class SignInDTO(
    val email: String,
    val password: String
)

