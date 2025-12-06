package com.apptive.japkor.data.constants

// ----------------------
// 성별
// ----------------------
enum class Gender(val value: String) {
    KOREAN_MALE("KOREAN_MALE"),
    JAPANESE_FEMALE("JAPANESE_FEMALE");
}

// ----------------------
// 흡연 여부
// ----------------------
enum class SmokingStatus(val value: String) {
    SMOKER("SMOKER"),
    NON_SMOKER("NON_SMOKER");
}

// ----------------------
// 음주 빈도
// ----------------------
enum class DrinkingFrequency(val value: String) {
    LESS_THAN_ONCE_A_WEEK("LESS_THAN_ONCE_A_WEEK"),
    ONCE_A_WEEK("ONCE_A_WEEK"),
    TWICE_A_WEEK("TWICE_A_WEEK"),
    MORE_THAN_THREE_TIMES_A_WEEK("MORE_THAN_THREE_TIMES_A_WEEK");
}

// ----------------------
// 종교
// ----------------------
enum class Religion(val value: String) {
    NONE("NONE"),
    BUDDHISM("BUDDHISM"),
    CHRISTIANITY("CHRISTIANITY"),
    CATHOLICISM("CATHOLICISM"),
    SHINTO("SHINTO"),
    OTHER("OTHER");
}

// ----------------------
// 학력
// ----------------------
enum class Education(val value: String) {
    HIGH_SCHOOL("HIGH_SCHOOL"),
    ASSOCIATE_DEGREE("ASSOCIATE_DEGREE"),
    BACHELOR_DEGREE("BACHELOR_DEGREE"),
    MASTER_DEGREE("MASTER_DEGREE"),
    DOCTORATE_DEGREE("DOCTORATE_DEGREE");
}

// ----------------------
// 본인 자산
// ----------------------
enum class Asset(val value: String) {
    UNDER_100M("UNDER_100M"),
    BETWEEN_100M_300M("BETWEEN_100M_300M"),
    BETWEEN_300M_500M("BETWEEN_300M_500M"),
    BETWEEN_500M_1B("BETWEEN_500M_1B"),
    OVER_1B("OVER_1B");
}

// ----------------------
// 선호 학벌
// ----------------------
enum class PreferredEducationLevel(val value: String) {
    TOP_TIER("TOP_TIER"),
    PRESTIGIOUS("PRESTIGIOUS"),
    MID_TIER("MID_TIER"),
    PRACTICAL("PRACTICAL");
}

// ----------------------
// 선호 외모 스타일
// ----------------------
enum class AppearanceStyle(val value: String) {
    CELEBRITY("CELEBRITY"),
    NATURAL("NATURAL"),
    UNIQUE("UNIQUE");
}

// ----------------------
// 부모님 자산 요구사항
// ----------------------
enum class ParentAssetRequirement(val value: String) {
    OVER_100M("OVER_100M"),
    RETIREMENT_ONLY("RETIREMENT_ONLY"),
    NO_CONCERN("NO_CONCERN");
}

// ----------------------
// MBTI 선호
// (한 Enum에 모든 축 포함: E/I/X, N/S, T/F, J/P)
// ----------------------
enum class MbtiPreference(val value: String) {
    E("E"), I("I"), X("X"),
    N("N"), S("S"),
    T("T"), F("F"),
    J("J"), P("P");
}

// ----------------------
// 우선순위
// ----------------------
enum class Priority(val value: String) {
    JOB("JOB"),
    EDUCATION("EDUCATION"),
    HEIGHT("HEIGHT"),
    APPEARANCE("APPEARANCE"),
    PARENT_ASSET("PARENT_ASSET"),
    ASSET("ASSET"),
    RELIGION("RELIGION"),
    PERSONALITY("PERSONALITY");
}

// ----------------------
// 직업
// (예시에 등장한 직업만 포함. 확장 가능)
// ----------------------
enum class Job(val value: String) {
    DOCTOR("DOCTOR"),
    TEACHER("TEACHER"),
    ENGINEER("ENGINEER"),
    FREELANCER("FREELANCER"),
    UNEMPLOYED("UNEMPLOYED");
}
