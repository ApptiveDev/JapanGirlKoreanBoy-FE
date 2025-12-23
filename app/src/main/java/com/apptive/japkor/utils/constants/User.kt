package com.apptive.japkor.utils.constants

enum class UserStatus {
    PENDING_APPROVAL, // 승인대기
    APPROVED, // 승인완료
    CONNECTING, // 연결중
    CONNECTED, // 연결됨
    BLACKLISTED // 블랙
}