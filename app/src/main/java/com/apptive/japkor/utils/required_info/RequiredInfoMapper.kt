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
        "흡연" -> "SMOKER"
        "X" -> "NON_SMOKER"
        "비흡연" -> "NON_SMOKER"
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
        "없음" -> "NONE"
        "무교" -> "NONE"
        "불교" -> "BUDDHISM"
        "기독교" -> "CHRISTIANITY"
        "천주교" -> "CATHOLICISM"
        "신토" -> "TOISM"
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
        "고등학교 졸업" -> "HIGH_SCHOOL"
        "고졸" -> "HIGH_SCHOOL"
        "전문학사(2년제 대학)" -> "ASSOCIATE_DEGREE"
        "전문학사 (2년제 대학)" -> "ASSOCIATE_DEGREE"
        "전문학사" -> "ASSOCIATE_DEGREE"
        "학사(4년제 대학)" -> "BACHELOR_DEGREE"
        "학사 (4년제 대학)" -> "BACHELOR_DEGREE"
        "학사" -> "BACHELOR_DEGREE"
        "석사" -> "MASTER_DEGREE"
        "박사" -> "DOCTORATE_DEGREE"
        else -> null
    }

    // -------------------------------
    // 자산
    // -------------------------------
    fun asset(koreanLabel: String): String? = when (koreanLabel) {
        "자산 1억 원 미만" -> "UNDER_100M"
        "1억 미만" -> "UNDER_100M"
        "자산 1억 ~ 3억 원 사이" -> "BETWEEN_100M_300M"
        "1억 ~ 3억" -> "BETWEEN_100M_300M"
        "자산 3억 ~ 5억 원 사이" -> "BETWEEN_300M_500M"
        "3억 ~ 5억" -> "BETWEEN_300M_500M"
        "자산 5억 ~ 10억 원 사이" -> "BETWEEN_500M_1B"
        "5억 ~ 10억" -> "BETWEEN_500M_1B"
        "자산 10억 원 초과" -> "OVER_1B"
        "10억 이상" -> "OVER_1B"
        else -> null
    }

    // -------------------------------
    // 선호 학벌
    // -------------------------------
    fun preferredEducation(koreanLabel: String): String? = when (koreanLabel) {
        "최상위권" -> "TOP_TIER"
        "상위권" -> "TOP_TIER"
        "명문대" -> "PRESTIGIOUS"
        "명문" -> "PRESTIGIOUS"
        "중상위권 (국공립 사립대)" -> "MID_TIER"
        "중위권" -> "MID_TIER"
        "실무 중심" -> "PRACTICAL"
        "실무형 (예술, 전문대)" -> "PRACTICAL"
        else -> null
    }

    // -------------------------------
    // 외모 스타일
    // -------------------------------
    fun appearanceStyle(koreanLabel: String): String? = when (koreanLabel) {
        "연예인 스타일" -> "CELEBRITY"
        "연예인 느낌의 비주얼" -> "CELEBRITY"
        "내추럴" -> "NATURAL"
        "자연스럽고 호감 가는 인상" -> "NATURAL"
        "유니크" -> "UNIQUE"
        "개성이 뚜렷하고 자기 스타일이 있는 사람" -> "UNIQUE"
        else -> null
    }

    // -------------------------------
    // 부모 자산 기준
    // -------------------------------
    fun parentAssetRequirement(koreanLabel: String): String? = when (koreanLabel) {
        "100만 엔 이상" -> "OVER_100M"
        "상속받을 자산이 1억 이상 있어야 해요" -> "OVER_100M"
        "연금만 있어도 됨" -> "RETIREMENT_ONLY"
        "노후관리만 되어 있으면 돼요" -> "RETIREMENT_ONLY"
        "노후 관리만 되어 있으면 돼요" -> "RETIREMENT_ONLY"
        "상관 없음" -> "NO_CONCERN"
        "상관없어요" -> "NO_CONCERN"
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
