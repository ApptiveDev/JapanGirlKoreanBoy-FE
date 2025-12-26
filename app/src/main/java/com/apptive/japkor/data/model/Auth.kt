package com.apptive.japkor.data.model


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

enum class UserStatus(val displayLabel: String) {
    INCOMPLETE_PROFILE("필수정보 미입력"), // 회원가입만 완료됨
    PENDING_APPROVAL("승인대기"), // 승인대기
    APPROVED("승인완료"), // 승인완료
    CONNECTING("연결중"), // 연결중
    CONNECTED("연결됨"), // 연결됨
    BLACKLISTED("블랙리스트입니다.") // 블랙리스트
}

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
