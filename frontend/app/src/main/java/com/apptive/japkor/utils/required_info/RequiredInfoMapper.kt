package com.apptive.japkor.utils.required_info

object RequiredInfoMapper {

    // -------------------------------
    // 성별
    // -------------------------------
    fun gender(koreanLabel: String): String? = when (koreanLabel) {
        "한국 남성" -> "KOREAN_MALE"
        "일본 여성" -> "JAPANESE_FEMALE"
        else -> null
    }

    // -------------------------------
    // 흡연 여부
    // -------------------------------
    fun smoking(koreanLabel: String): String? = when (koreanLabel) {
        "O" -> "SMOKER"
        "X" -> "NON_SMOKER"
        else -> null
    }

    // -------------------------------
    // 음주 빈도
    // -------------------------------
    fun drinking(koreanLabel: String): String? = when (koreanLabel) {
        "주 1회 미만" -> "LESS_THAN_ONCE_A_WEEK"
        "주 1회" -> "ONCE_A_WEEK"
        "주 2회" -> "TWICE_A_WEEK"
        "주 3회 이상" -> "MORE_THAN_THREE_TIMES_A_WEEK"
        else -> null
    }

    // -------------------------------
    // 종교
    // -------------------------------
    fun religion(koreanLabel: String): String? = when (koreanLabel) {
        "무교" -> "NONE"
        "불교" -> "BUDDHISM"
        "기독교" -> "CHRISTIANITY"
        "천주교" -> "CATHOLICISM"
        "신토" -> "SHINTO"
        "기타" -> "OTHER"
        else -> null
    }

    // 기피 종교 (리스트)
    fun avoidReligionList(labels: List<String>): List<String>? =
        labels.mapNotNull { religion(it) }

    // -------------------------------
    // 학력
    // -------------------------------
    fun education(koreanLabel: String): String? = when (koreanLabel) {
        "고졸" -> "HIGH_SCHOOL"
        "전문학사" -> "ASSOCIATE_DEGREE"
        "학사" -> "BACHELOR_DEGREE"
        "석사" -> "MASTER_DEGREE"
        "박사" -> "DOCTORATE_DEGREE"
        else -> null
    }

    // -------------------------------
    // 자산
    // -------------------------------
    fun asset(koreanLabel: String): String? = when (koreanLabel) {
        "1억 미만" -> "UNDER_100M"
        "1억 ~ 3억" -> "BETWEEN_100M_300M"
        "3억 ~ 5억" -> "BETWEEN_300M_500M"
        "5억 ~ 10억" -> "BETWEEN_500M_1B"
        "10억 이상" -> "OVER_1B"
        else -> null
    }

    // -------------------------------
    // 선호 학벌
    // -------------------------------
    fun preferredEducation(koreanLabel: String): String? = when (koreanLabel) {
        "최상위권" -> "TOP_TIER"
        "명문대" -> "PRESTIGIOUS"
        "중위권" -> "MID_TIER"
        "실무 중심" -> "PRACTICAL"
        else -> null
    }

    // -------------------------------
    // 외모 스타일
    // -------------------------------
    fun appearanceStyle(koreanLabel: String): String? = when (koreanLabel) {
        "연예인 스타일" -> "CELEBRITY"
        "내추럴" -> "NATURAL"
        "유니크" -> "UNIQUE"
        else -> null
    }

    // -------------------------------
    // 부모 자산 기준
    // -------------------------------
    fun parentAssetRequirement(koreanLabel: String): String? = when (koreanLabel) {
        "100만 엔 이상" -> "OVER_100M"
        "연금만 있어도 됨" -> "RETIREMENT_ONLY"
        "상관 없음" -> "NO_CONCERN"
        else -> null
    }

    // -------------------------------
    // 우선순위
    // -------------------------------
    fun priority(koreanLabel: String): String? = when (koreanLabel) {
        "직업" -> "JOB"
        "학력" -> "EDUCATION"
        "키" -> "HEIGHT"
        "외모" -> "APPEARANCE"
        "부모 자산" -> "PARENT_ASSET"
        "본인 자산" -> "ASSET"
        "종교" -> "RELIGION"
        "성격" -> "PERSONALITY"
        else -> null
    }

    // -------------------------------
    // 직업
    // -------------------------------
    fun job(koreanLabel: String): String? = when (koreanLabel) {
        "의사" -> "DOCTOR"
        "교사" -> "TEACHER"
        "엔지니어" -> "ENGINEER"
        "프리랜서" -> "FREELANCER"
        "무직" -> "UNEMPLOYED"
        else -> null
    }
}
