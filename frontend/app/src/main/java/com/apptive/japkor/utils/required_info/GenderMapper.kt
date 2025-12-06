package com.apptive.japkor.utils.required_info

object GenderMapper {
    fun toServerValue(koreanLabel: String): String? {
        return when (koreanLabel) {
            "한국 남성" -> "KOREAN_MALE"
            "일본 여성" -> "JAPANESE_FEMALE"
            else -> null
        }
    }
}