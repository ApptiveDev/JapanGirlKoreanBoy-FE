package com.apptive.japkor.data.model

data class MatchingResponse(
    val matchingId: Long,
    val maleMemberId: Long,
    val maleName: String,
    val maleEmail: String,
    val height: Int?,
    val weight: Int?,
    val residenceArea: String?,
    val matchingOrder: Int,
    val status: String
)
