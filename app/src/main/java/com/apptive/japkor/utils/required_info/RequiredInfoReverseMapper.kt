package com.apptive.japkor.utils.required_info

object RequiredInfoReverseMapper {

    // -------------------------------
    // 흡연 여부
    // -------------------------------
    private val smokingMap = mapOf(
        "SMOKER" to "흡연",
        "NON_SMOKER" to "비흡연"
    )

    fun smoking(code: String?): String =
        smokingMap[code] ?: "-"

    // -------------------------------
    // 음주 빈도
    // -------------------------------
    private val drinkingMap = mapOf(
        "LESS_THAN_ONCE_A_WEEK" to "주 1회 미만",
        "ONCE_A_WEEK" to "주 1회",
        "TWICE_A_WEEK" to "주 2회",
        "MORE_THAN_THREE_TIMES_A_WEEK" to "주 3회 이상"
    )

    fun drinking(code: String?): String =
        drinkingMap[code] ?: "-"

    // -------------------------------
    // 종교
    // -------------------------------
    private val religionMap = mapOf(
        "NONE" to "무교",
        "BUDDHISM" to "불교",
        "CHRISTIANITY" to "기독교",
        "CATHOLICISM" to "천주교",
        "TOISM" to "신토",
        "OTHER" to "기타"
    )

    fun religion(code: String?): String =
        religionMap[code] ?: "-"

    // -------------------------------
    // 학력
    // -------------------------------
    private val educationMap = mapOf(
        "HIGH_SCHOOL" to "고졸",
        "ASSOCIATE_DEGREE" to "전문학사",
        "BACHELOR_DEGREE" to "학사",
        "MASTER_DEGREE" to "석사",
        "DOCTORATE_DEGREE" to "박사"
    )

    fun education(code: String?): String =
        educationMap[code] ?: "-"

    // -------------------------------
    // 자산
    // -------------------------------
    private val assetMap = mapOf(
        "UNDER_100M" to "1억 미만",
        "BETWEEN_100M_300M" to "1억 ~ 3억",
        "BETWEEN_300M_500M" to "3억 ~ 5억",
        "BETWEEN_500M_1B" to "5억 ~ 10억",
        "OVER_1B" to "10억 이상"
    )

    fun asset(code: String?): String =
        assetMap[code] ?: "-"

    // -------------------------------
    // 직업
    // -------------------------------
    private val jobMap = mapOf(
        "DOCTOR" to "의사",
        "PHARMACIST" to "약사",
        "NURSE" to "간호사",
        "TEACHER" to "교사",
        "PROFESSOR" to "교수",
        "LAWYER" to "변호사",
        "ACCOUNTANT" to "회계사",
        "ENGINEER" to "엔지니어",
        "DEVELOPER" to "개발자",
        "DESIGNER" to "디자이너",
        "CIVIL_SERVANT" to "공무원",
        "POLICE" to "경찰",
        "FIREFIGHTER" to "소방관",
        "MILITARY" to "군인",
        "ENTREPRENEUR" to "사업가",
        "CEO" to "경영인",
        "FINANCE" to "금융",
        "RESEARCHER" to "연구원",
        "ARTIST" to "예술가",
        "ATHLETE" to "운동선수",
        "FREELANCER" to "프리랜서",
        "STUDENT" to "학생",
        "UNEMPLOYED" to "무직",
        "ANY" to "상관없음",
        "OTHER" to "기타"
    )

    fun job(code: String?): String =
        jobMap[code] ?: "-"
}
